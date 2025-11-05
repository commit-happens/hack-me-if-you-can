import useTranslation from "../../hooks/useTranslation";
import type { BasePageProps } from "../../models/BasePageProps";
import Page from "../../models/Page";

function Header(props: BasePageProps) {
  const { page, navigate } = props;
  const texts = useTranslation("app");

  if (!page || page === Page.Welcome) {
    return <>{texts.titleAbbr}</>;
  }

  return (
    <a
      href=""
      onClick={(e) => {
        e.preventDefault();

        if (!navigate) return;
        navigate(Page.Welcome);
      }}
    >
      {texts.titleAbbr}
    </a>
  );
}
export default Header;
