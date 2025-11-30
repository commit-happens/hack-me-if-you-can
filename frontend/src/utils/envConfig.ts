/**
 * Z konfiguračního souboru načte hodnotu podle klíče.
 * @param key  Klíč v konfiguračním souboru.
 * @param defaultValue Výchozí hodnota, pokud klíč neexistuje.
 * @returns Hodnota z konfiguračního souboru nebo výchozí hodnota.
 */
export function getEnvConfigValue<T extends string | number | boolean>(
  key: string,
  defaultValue: T,
): T {
  const value = import.meta.env[key];

  if (value === undefined) {
    return defaultValue;
  }

  if (typeof defaultValue === "number") {
    const num = Number(value);
    return (isNaN(num) ? defaultValue : num) as T;
  }

  if (typeof defaultValue === "boolean") {
    return (value === "true") as T;
  }

  return value as T;
}
