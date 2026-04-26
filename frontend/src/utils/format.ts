export const formatNumber = (
  value: number,
  locale: string = "cs-CZ",
  options?: Intl.NumberFormatOptions,
): string => {
  return Intl.NumberFormat(locale, options).format(value);
};
