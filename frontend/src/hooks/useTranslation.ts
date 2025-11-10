import type Locale from "../models/Locale";
// Import jazyků
import csCZ, { type Translation } from "../languages/csCZ";

const defaultLocale: Locale = import.meta.env.VITE_API_LOCALE || "csCZ";

const translations: Partial<Record<Locale, Translation>> = {
  csCZ,
};

const selectedTranslation = translations[defaultLocale] || csCZ;

const useTranslation = <K extends keyof Translation>(viewKey: K) => {
  const translationTexts: Translation[K] = selectedTranslation[viewKey];
  return translationTexts;
};

export default useTranslation;
