import type Page from "../models/Page";
import { routes } from "../routing/routes";

/**
 * Vrátí cestu (path) pro danou stránku.
 * @param page Stránka, pro kterou chceme získat cestu.
 * @returns Cesta (path) jako řetězec.
 */
export function getPagePath(page: Page) {
  const route = routes[page];
  return route.path;
}
