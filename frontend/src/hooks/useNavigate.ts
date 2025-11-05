import { useState } from "react";
import Page from "../models/Page";

const useNavigate = () => {
  const [page, setPage] = useState<Page>(Page.Welcome);

  const navigate = (newPage: Page) => {
    setPage(newPage);
  };

  return { page, navigate };
};

export default useNavigate;
