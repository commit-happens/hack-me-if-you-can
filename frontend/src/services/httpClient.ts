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

/**
 * Odešle HTTP požadavek přes ky a vrátí JSON odpověď v požadovaném typu.
 *
 * Query parametry převádí na `URLSearchParams`; `null` serializuje jako řetězec "null"
 * a `undefined` parametry vynechá.
 *
 * Při neúspěšné odpovědi skládá chybovou zprávu v pořadí: `fields.nickname`, `error`,
 * `message`, jinak použije fallback s HTTP statusem.
 *
 * @template TData Typ JSON odpovědi, který volající očekává.
 * @param config Konfigurace požadavku (URL, metoda, hlavičky, query parametry, payload).
 * @param options Volitelné přepsání signalu pro zrušení konkrétního požadavku.
 * @returns Deserializovaná JSON odpověď typu `TData`.
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
    const errorBody = (await response.json().catch(() => null)) as ApiError | null;
    const message =
      errorBody?.fields?.nickname ||
      errorBody?.error ||
      errorBody?.message ||
      `Chyba požadavku: ${response.status}`;

    throw new Error(message);
  }

  return response.json<TData>();
};
