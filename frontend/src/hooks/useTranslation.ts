import type Locale from "../models/Locale";
// Import jazyků
import csCZ, { type Translation } from "../languages/csCZ";
import { getEnvConfigValue } from "../utils/envConfig";

const defaultLocale: Locale = getEnvConfigValue(
  "VITE_API_LOCALE",
  "csCZ",
) as Locale;

const translations: Partial<Record<Locale, Translation>> = {
  csCZ,
};

const selectedTranslation = translations[defaultLocale] || csCZ;

const useTranslation = <K extends keyof Translation>(viewKey: K) => {
  const translationTexts: Translation[K] = selectedTranslation[viewKey];
  return translationTexts;
};

// Funkce nahradí placeholdery ve formátu {1}, {2}, ... odpovídajícími hodnotami
export function getText(text: string, values?: (string | number)[]) {
  if (!values) return text;

  return values.reduce((acc: string, value, index) => {
    const placeholder = `{${index + 1}}`;
    return acc.replaceAll(placeholder, String(value));
  }, text);
}

export default useTranslation;
