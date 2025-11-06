import { Link, useLocation } from "react-router-dom";
import useTranslation from "../../hooks/useTranslation";
import Page from "../../models/Page";
import { getPagePath } from "../../utils/routing";

function Header() {
  const { pathname } = useLocation();
  const texts = useTranslation("app");

  if (pathname === getPagePath(Page.Welcome)) {
    return <>{texts.titleAbbr}</>;
  }

  return <Link to="/">{texts.titleAbbr}</Link>;
}
export default Header;
