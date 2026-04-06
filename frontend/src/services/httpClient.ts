import ky from "ky";
import { getEnvConfigValue } from "../utils/envConfig";

/** Konfigurace HTTP požadavku předaného do API klienta. */
type HttpClientConfig = {
  url: string;
  method: string;
  headers?: HeadersInit;
  params?: Record<string, string | number | boolean | null | undefined>;
  data?: unknown;
  signal?: AbortSignal;
};

/** Volitelné runtime nastavení pro konkrétní volání klienta. */
type HttpClientOptions = {
  signal?: AbortSignal;
};

/** Očekávaný tvar chybové odpovědi z API. */
type ApiError = {
  error?: string;
  message?: string;
  fields?: {
    nickname?: string;
  };
};

/** Vrátí true, pokud je odpověď JSON podle Content-Type hlavičky. */
const isJsonResponse = (response: Response): boolean => {
  const contentType = response.headers.get("content-type")?.toLowerCase() ?? "";
  return contentType.includes("application/json") || contentType.includes("+json");
};

/** Načte tělo odpovědi s podporou JSON, textu i prázdných odpovědí. */
const parseResponseBody = async (response: Response): Promise<unknown> => {
  if (response.status === 204 || response.status === 205 || response.status === 304) {
    return undefined;
  }

  if (response.headers.get("content-length") === "0") {
    return undefined;
  }

  const rawBody = await response.text();
  if (!rawBody.trim()) {
    return undefined;
  }

  if (isJsonResponse(response)) {
    try {
      return JSON.parse(rawBody);
    } catch {
      return undefined;
    }
  }

  return rawBody;
};

/** Type guard pro očekávaný tvar API chyby. */
const isApiError = (value: unknown): value is ApiError => {
  return value !== null && typeof value === "object";
};

/**
 * Odešle HTTP požadavek přes `ky` a vrátí odpověď v požadovaném typu.
 *
 * Query parametry převádí na `URLSearchParams`; `null` serializuje jako řetězec "null"
 * a `undefined` parametry vynechá.
 *
 * Při neúspěšné odpovědi skládá chybovou zprávu v pořadí: `fields.nickname`, `error`,
 * `message`, jinak použije fallback s HTTP statusem.
 *
 * @template TData Typ odpovědi, který volající očekává (JSON, text, nebo prázdné tělo).
 * @param config Konfigurace požadavku (URL, metoda, hlavičky, query parametry, payload).
 * @param options Volitelné přepsání signalu pro zrušení konkrétního požadavku.
 * @returns Deserializovaná odpověď typu `TData`.
 */
export const httpClient = async <TData>(
  config: HttpClientConfig,
  options?: HttpClientOptions,
): Promise<TData> => {
  const apiBaseUrl = getEnvConfigValue("VITE_API_BASE_URL", "");

  const baseUrl = `${apiBaseUrl}${config.url}`;

  const searchParams = new URLSearchParams();
  if (config.params) {
    Object.entries(config.params).forEach(([key, value]) => {
      if (value === null) {
        searchParams.append(key, "null");
      } else if (value !== undefined) {
        searchParams.append(key, String(value));
      }
    });
  }

  const url = searchParams.toString() ? `${baseUrl}?${searchParams.toString()}` : baseUrl;

  const response = await ky(url, {
    method: config.method,
    headers: config.headers,
    json: config.data,
    throwHttpErrors: false,
    signal: options?.signal ?? config.signal,
  });

  if (!response.ok) {
    const parsedErrorBody = await parseResponseBody(response);
    const errorBody = isApiError(parsedErrorBody) ? parsedErrorBody : null;
    const stringError = typeof parsedErrorBody === "string" ? parsedErrorBody : undefined;
    const message =
      errorBody?.fields?.nickname ||
      errorBody?.error ||
      errorBody?.message ||
      stringError ||
      `Chyba požadavku: ${response.status}`;

    throw new Error(message);
  }

  return (await parseResponseBody(response)) as TData;
};
