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
  return value !== undefined ? value : defaultValue;
}
