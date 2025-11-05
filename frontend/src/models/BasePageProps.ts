import type Page from "./Page";

export type BasePageProps = {
  page?: Page;
  navigate?: (page: Page) => void;
};
