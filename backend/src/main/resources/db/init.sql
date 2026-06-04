-- Init Players table
INSERT INTO players (nickname)
VALUES
    ('singhaTheRulerOfAllWorlds001'),
    ('CyberEli'),
    ('NikByte'),
    ('ReneRoot'),
    ('Čičina♥'),
    ('Král 🐸 žabák'),
    ('ZeroTrust'),
    ('   náhodný   mezery   '),
    ('Cloud_buster'),
    ('d1v0c4k_!!!'),
    ('xX_no0bSl4yer_Xx!!!🔥'),
    ('🧅🥲 cebulka_master'),
    ('   omylem   mezery    všude   '),
    ('D3F4ULT___USR???'),
    ('😈💀 FINAL_BOSS_420 💀😈'),
    ('hej!proc_to_nefunguje??'),
    ('kOcOuR_žÍžAlA_88'),
    ('XDxdXDxdXd   '),
    ('pr@šiv#ák!_69'),
    ('🌪️..tornado_homyx..🌪️'),
    ('🤖_MegaUltraBotDestroyer9000_⚡'),
    ('shadow_king_of_darkness_🔥💀'),
    ('☠️xXx_FATALITY_MASTER_3000_xXx'),
    ('superLongNickName_becauseWHY?!'),
    ('🍕HungryPlayerWhoEatsAllTime🍕'),
    ('ERROR_404_username_not_found😵'),
    ('⚔️KnightOfTheLaggingRealm_123⚔️'),
    ('🐸spong3b0b_m3m3_L0RD_ultimate🐸'),
    ('🔥🔥unlimited_power_overflow_99'),
    ('Too_Many_Characters_But_Fine😅'),
    ('Žluťoučký_Kůň✨'),
    ('lol_xd_🤡'),
    ('R@nd0mUser?!!'),
    ('UTF8_Šílenec💥'),
    ('404nicknameNotHere'),
    (';;;DROP_TABLE_USER;;;'),
    ('Čundrák_v_lese🌲'),
    ('za_malo😂_pane'),
    ('xXx_xoxo_kitty_xXx'),
    ('🥒nakl@dačka🥒'),
    ('mega⚡lagger9000'),
    ('žádnej_nápad😂'),
    ('🧅OnionSlayer🧅'),
    ('pepeLaugh🤣'),
    ('mrk3v_kungfu🥋'),
    ('Z0X'),
    ('🔥🐸👑💀'),
    ('Lord_Cyber_Tatínek3000'),
    ('MalySniper007Pro'),
    ('N1ghtM4re_Ex3cut0r'),
    ('gͧͣ͌ͫͬl̶̶ͬi̷̸ͯt̷ͤc̶͒͗̎h͐͒͝'),
    ('SakraTyJsiTrdloXD'),
    ('"DROP TABLE users;--"'),
    ('P0m3l0_Quantum_Ninja'),
    ('🔥'),
    ('💀'),
    ('🐸'),
    ('?!'),
    ('⚡'),
    ('XD'),
    ('🧅'),
    ('🤖'),
    ('🔥🐸'),
    ('_'),
    ('Q'),
    ('🌀'),
    ('💣BOOM'),
    ('F1reFly✨'),
    ('N@h0dny_Zn@k'),
    ('m͑ͩ̒͗͒̾̾a͐ͤ̽ͪͥͨs̓̄̋̌͂̇oͧ͊̏̍'),
    ('‏‏‎ ‎InvisibleBoy‎‏‏‎ ‎'),
    ('zero width spaces'),
    ('longggggggggggggggggggggggggg🤯'),
    ('Aͯ̄͗͆ͭͣͨ͛ͤ̋ͤ̅̎'),
    ('🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥'),
    ('line\nbreak'),
    ('　　　lots_of_spaces　　　'),
    ('RTL_‏‏‎test‏‏‎_🔥'),
    ('𝕊𝕦𝕡𝕖𝕣𝔽𝕠𝕟𝕥'),
    ('xss_test_escaped'),
    ('''quotes_everywhere'''),
    ('/////////'),
    ('🐛bug🐛bug🐛bug🐛'),
    ('🤡ClownMode🤡'),
    ('😇🙃🙂😉😏'),
    ('❗❗❗ERROR❗❗❗'),
    ('„‚…†‡‰Š‹ŒžŸ'),
    ('emoji‍‍‍cluster‍‍‍test'),
    ('dvojity̌̌test'),
    ('¯\_(ツ)_/¯')
ON CONFLICT (nickname) DO NOTHING;

-- Init Phishing Categories table
INSERT INTO phishing_categories (tag, name, description, reward_points, security_hints)
VALUES ('LEGIT',
        'Legitimní zpráva / Bezpečný obsah',
        'Zprávy, které jsou autentické, pocházejí od důvěryhodných odesílatelů a neobsahují žádné škodlivé prvky.',
        100,
        '[
          "Doména odesílatele (část za zavináčem) přesně odpovídá oficiální webové adrese služby (např. @alza.cz, @paypal.com, @csob.cz).",
          "Zpráva je očekávaná a přímo související s vaší nedávnou aktivitou (např. potvrzení objednávky, kterou jste právě odeslali, nebo kód pro obnovu hesla, o který jste sami požádali).",
          "Odkazy po najetí myší (hover) směřují přímo na oficiální doménu služby bez podezřelých poddomén nebo zkracovačů (jako bit.ly či tinyurl) v citlivé komunikaci.",
          "Oficiální instituce vás nikdy nežádají o zaslání citlivých údajů (heslo, PIN, CVV kód) přímo v textu zprávy ani přes odkaz v ní.",
          "Zpráva postrádá prvky manipulace, jako je uměle vyvolaný strach, časový nátlak nebo sliby nereálných výher.",
          "Komunikace je profesionální, bez gramatických chyb a s logickým formátováním, které odpovídá vizuální identitě dané značky."
        ]'::jsonb),
       ('FAKE_URL',
        'Falešné URL / Neodpovídající doména',
        'Útoky využívající vizuální podobnost adres nebo podvržené odkazy, které vypadají jako legitimní, ale směřují jinam.',
        600,
        '[
          "Vždy najeď myší nad odkaz (hovering) a sleduj levý dolní roh prohlížeče. Tam uvidíš skutečný cíl, který se může lišit od textu, na který klikáš.",
          "Čti doménu zprava doleva. Hlavní doména je ta těsně před koncovkou (.cz, .com). Například u `microsoft.security-update.com` je skutečným majitelem `security-update.com`, nikoliv Microsoft.",
          "Hledej drobné záměny znaků (tzv. typosquatting). Útočníci často mění `m` za `rn`, `l` za `1`, nebo přidávají nenápadnou pomlčku (např. `ceska-posta.cz` místo `ceskaposta.cz`).",
          "Nenech se zmást symbolem zámku (HTTPS). Zelený zámek znamená pouze to, že spojení je šifrované, nikoliv že web patří důvěryhodné firmě.",
          "Pozor na zkrácené odkazy (např. bit.ly, tinyurl). Oficiální instituce jako banky nebo státní úřady je v e-mailech k přihlašování téměř nikdy nepoužívají.",
          "Pokud odkaz vede na IP adresu (např. http://192.168.1.1/login), je to téměř jistě podvod."
        ]'::jsonb),
       ('URGENT',
        'Urgentní žádost / Sociální inženýrství',
        'Manipulativní techniky, které zneužívají autoritu, strach z postihu nebo časový nátlak k vynucení neuvážené akce.',
        500,
        '[
          "Zpozorni u každé zprávy, která vyžaduje „okamžitou akci“, jinak hrozí „smazání účtu“, „pokuta“ nebo „právní postih“. Časový nátlak je hlavním nástrojem útočníků.",
          "Ověřuj identitu odesílatele jiným kanálem. Pokud ti píše šéf nebo kolega neobvyklou žádost (např. o platbu nebo zaslání údajů), raději mu zavolej nebo napiš na interní chat.",
          "Sleduj tón zprávy. Působí e-mail nezvykle familiárně, nebo naopak příliš agresivně? Útočníci často neznají přesný styl komunikace ve vaší firmě.",
          "Buď podezřívavý k požadavkům na nestandardní platební metody (např. nákup dárkových karet, kryptoměny nebo převod na soukromý účet bankéře).",
          "Všímej si drobností v oslovení. Legitimní firmy tě většinou oslovují jménem, zatímco phishing často používá obecné „Vážený zákazníku“ nebo „Drahý uživateli“.",
          "Pamatuj, že banka ani státní úřad po tobě nikdy nebudou chtít heslo nebo PIN přes e-mail, ani tě nebudou nutit k převodu peněz pod záminkou „napadeného účtu“."
        ]'::jsonb),
       ('FAKE_DOC',
        'Falešná faktura / Falešný dokument',
        'Podvodné e-maily obsahující přílohy nebo odkazy na dokumenty, které mají za cíl infikovat zařízení virem nebo vylákat platební údaje.',
        700,
        '[
          "Nečekané faktury neotevírej. Pokud ti přijde faktura od služby, kterou nevyužíváš (např. jiný mobilní operátor), je to jasný varovný signál.",
          "Kontroluj přípony souborů. Útočníci často maskují spustitelné soubory jako dokumenty (např. `faktura.pdf.exe` nebo `uctenka.zip`). Pokud přípona neodpovídá typu dokumentu, soubor nespouštěj.",
          "Nikdy nepovoluj makra v dokumentech (Word, Excel). Pokud tě dokument po otevření vyzve k „povolení obsahu“ nebo „aktivaci maker“, okamžitě jej zavři – je to cesta k instalaci viru.",
          "Ověř si odesílatele. Faktura od velké firmy (např. ČEZ, Alza, O2) nikdy nepřijde z podezřelé nebo bezplatné adresy (např. `faktura123@seznam.cz` nebo `admin@eu-podpora.net`).",
          "Srovnej detaily. Pokud se částka nebo variabilní symbol neshoduje s tvými předchozími platbami, které najdeš v oficiálním internetovém bankovnictví nebo klientské zóně, je to podvod.",
          "Místo přílohy použij oficiální portál. Bezpečnější než klikat na odkaz v e-mailu je přihlásit se přímo do klientského portálu dané firmy a fakturu si stáhnout tam."
        ]'::jsonb),
       ('CRED_THEFT',
        'Sběr přihlašovacích údajů (falešný login)',
        'Podvodné stránky, které vypadají jako věrná kopie přihlašovacích obrazovek známých služeb (Google, Microsoft, banky), s cílem ukrást vaše heslo a 2FA kódy.',
        900,
        '[
          "Adresní řádek je jediný nezfalšovatelný důkaz. Vždy zkontroluj, zda doména přesně odpovídá (např. `accounts.google.com` vs. `accounts-google.net`).",
          "Pamatuj, že vizuální stránka se kopíruje nejsnadněji. To, že vidíš logo své banky a známé barvy, neznamená, že jsi na jejím webu.",
          "Zpozorni, pokud tě e-mail nutí k přihlášení pod záminkou „neobvyklé aktivity“ nebo „nutné aktualizace údajů“. Legitimní služby tě raději vyzvou, abys šel do jejich aplikace.",
          "Najeď myší na tlačítko „Přihlásit se“ (hover) ještě v e-mailu. Pokud adresa dole v rohu prohlížeče vypadá podezřele nebo je velmi dlouhá a nepřehledná, neklikej.",
          "Používej správce hesel. Pokud máš uložené heslo pro daný web, správce ho na falešné doméně automaticky nevyplní – to je okamžité varování, že jsi na podvodu.",
          "Aktivuj si dvoufázové ověření (2FA). I když útočník získá tvé heslo přes falešný login, bez kódu z mobilu nebo fyzického klíče se k účtu nedostane."
        ]'::jsonb),
       ('SPEAR_PHISH',
        'Personalizovaný útok (sociální inženýrství na míru)',
        'Vysoce cílený útok (Spear Phishing), který využívá konkrétní informace o vás (jméno, pozice, kolegové, nedávné aktivity), aby působil maximálně důvěryhodně.',
        1000,
        '[
          "To, že odesílatel zná vaše jméno nebo pracovní pozici, neznamená, že je zpráva bezpečná. Tyto údaje lze snadno získat z LinkedInu nebo firemních webů.",
          "Prověřte komunikační kanál. Pokud vám ředitel firmy (CEO), se kterým běžně nemluvíte, najednou píše přímý e-mail s urgentní žádostí, je to extrémně podezřelé.",
          "Sledujte detaily, které „nesedí“. Používá kolega v e-mailu tykání, i když si běžně vykáte? Je tón zprávy nezvykle formální nebo naopak příliš urgentní?",
          "Buďte opatrní na zmínky o konkrétních událostech. Útočníci často zmiňují konference nebo projekty, kterých jste se účastnili, aby získali vaši důvěru.",
          "Vždy si neobvyklou žádost potvrďte jinou cestou (osobně, telefonem, interním chatem). Nepoužívejte kontaktní údaje uvedené přímo v podezřelém e-mailu.",
          "Pamatujte, že útočník může napodobit i e-mailovou adresu vašeho kolegy (tzv. spoofing). Pokud se vám obsah zprávy nezdá, nevěřte ani jménu odesílatele."
        ]'::jsonb),
       ('LOTTERY',
        'Soutěžní / loterijní phishing',
        'Zprávy slibující lákavé výhry (iPhony, peněžní obnosy, dárkové poukazy), které se snaží vylákat platební údaje nebo osobní data pod záminkou "doručení výhry".',
        250,
        '[
          "Základní pravidlo: Pokud jste se žádné soutěže aktivně neúčastnili, nemůžete v ní vyhrát. Žádná firma nerozdává hodnotné ceny náhodným lidem na internetu.",
          "Pozor na poplatky předem. Legitimní soutěže nikdy nevyžadují zaplacení „manipulačního poplatku“, „pojištění“ nebo „cla“ pro uvolnění výhry.",
          "Kontrolujte doménu odesílatele. Vyhráli jste u Alzy, ale e-mail přišel z adresy `info@vyhra-pro-vas.xyz`? Je to podvod.",
          "Nikdy nezadávejte údaje o platební kartě pro „ověření věku“ nebo „zaplacení poštovného“ u výhry, která má být zdarma. Právě o tyto údaje útočníkům jde.",
          "Sledujte časový nátlak. Výzvy typu „Máte jen 2 minuty na vyzvednutí své ceny“ mají zabránit vašemu kritickému myšlení.",
          "Ověřte si soutěž na oficiálních stránkách nebo sociálních sítích dané firmy. Velké kampaně jsou tam vždy dohledatelné."
        ]'::jsonb)
ON CONFLICT (tag) DO NOTHING;

-- Init Questions table
INSERT INTO questions (platform_type_id,
                       is_phishing,
                       phishing_category_id,
                       difficulty,
                       penalty,
                       content,
                       metadata,
                       explanation)
VALUES (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'CRED_THEFT'),
        1,
        10,
        'Vážený zákazníku, zaznamenali jsme neobvyklou aktivitu na vašem účtu. Klikněte prosím na tento odkaz a ověřte své údaje: [Ověřit účet](http://mybank-support.verify-login.example/confirm). Pokud neověříte do 24 hodin, dojde k omezení přístupu.',
        jsonb_build_object('sender', 'security@mybank-support.com', 'subject', 'Okamžité ověření účtu vyžadováno'),
        'Adresa odesílatele a URL neodpovídají oficiální doméně banky (obsahují navíc ''- support'' a podivnou doménu). E-mail vyvolává umělý tlak časovým limitem (urgentní akce).'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Dobrý den,  Vaše objednávka byla přijata a bude odeslána do 2 pracovních dnů. Vaše objednávky si můžete prohlédnout ve vašem [uživatelském účtu](https://www.eshop-novinky.cz/user-47/orders). V příloze naleznete potvrzení objednávky.  S pozdravem, Tým e-shopu',
        jsonb_build_object('sender', 'info@eshop-novinky.cz', 'subject', 'Děkujeme za objednávku č. 23567'),
        'Tento e-mail je pravý: odesílatel i předmět vypadají standardně, žádný odkaz nevyžaduje akci, v textu není žádná žádost o citlivé údaje. (Přílohu doporučujeme otevírat pouze pokud očekáváte platbu/objednávku.)'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'URGENT'),
        1,
        8,
        'Dobrý den,  v příloze zasíláme fakturu k okamžité úhradě. [Zaplaťte pohodlně online](http://pay-now.example/payment?id=78945). Pokud nebyla faktura očekávána, kontaktujte nás na +420 123 456 789.',
        jsonb_build_object('sender', 'uctar@dodavatel-faktury.eu', 'subject', 'Faktura č. 78945 - SPLATNOST DNES!'),
        'E-mail žádá o okamžitou platbu a obsahuje odkaz s nejasnou doménou. Pokud nejste očekávaný příjemce této faktury, ověřte informace přímo u známého kontaktu dodavatele. (Příloha a odkaz mohou být škodlivé.)'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'FAKE_URL'),
        1,
        3,
        'Vážený uživateli,  aktualizovali jsme podmínky používání služby. [Přihlaste se a potvrďte změny](https://example.org/update).',
        jsonb_build_object('sender', 'notifications@sluzby.example.org', 'subject', 'Aktualizace podmínek služby'),
        'URL obsahuje správnou doménu, ale odesílatel ''notifications@sluzby.example.org'' je obecný a v textu chybí personalizace. Před kliknutím ověřte, zda jste skutečně registrovaným uživatelem a zda adresa odpovídá oficiální adrese služby.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'URGENT'),
        1,
        9,
        'Ahoj, administrátor požádal o reset vašeho hesla. Prosím [ověřte svou identitu](http://firma-corp.reset-password.example). Pokud jste o požadavek nežádali, ignorujte tento e-mail.',
        jsonb_build_object('sender', 'it-helpdesk@firma-corp.com', 'subject', 'Reset hesla - akce vyžadována'),
        'Adresa odkazu obsahuje ''reset-password'' na podezřelé doméně, která není oficiální firemní doménou. Také chybí další informace pro ověření požadavku. To jsou typické znaky podvrženého požadavku na přihlášení.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Dobrý den,  v tomto týdnu pro vás máme výběr nejzajímavějších [článků a tipů](https://technews.example/articles). Přejeme příjemné čtení. Žádné akce nejsou vyžadovány.',
        jsonb_build_object('sender', 'newsletter@technews.example', 'subject',
                           'Týdenní přehled: Novinky v technologiích'),
        'Typický newsletter - neobsahuje naléhavé požadavky, odkazy vedou na články (pokud klikáte, ověřte cílové URL).'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'SPEAR_PHISH'),
        1,
        7,
        'Dobrý den,  žádáme o potvrzení nových platebních údajů. Pro ověření klikněte [zde](https://billing.dodavatel.example/verify?token=XYZ). Pokud si tuto změnu nepamatujete, ihned kontaktujte support.',
        jsonb_build_object('sender', 'support@dodavatel-služeb.example', 'subject', 'Potvrďte změnu platebních údajů'),
        'I když odkaz vypadá jako ''billing.dodavatel.example'', parametr token v URL a žádost o potvrzení platebních údajů může znamenat pokus o získání přihlašovacích nebo platebních dat. Ověřte změnu přímo u známeho kontaktu dodavatele, ne přes vložený odkaz.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Vážení rodiče,  zveme Vás na rodičovské setkání ve čtvrtek v 17:00 ve školní jídelně. Prosíme o potvrzení účasti odpovědí na tento e-mail.',
        jsonb_build_object('sender', 'kontakt@skola-obec.cz', 'subject', 'Informace o plánovaném rodičovském setkání'),
        'Legitimní komunikace školy - žádost o jednoduchou odpověď bez odkazů či příloh. Pokud by bylo v textu něco neočekávaného (např. žádost o platbu), ověřte telefonicky u školy.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'FAKE_URL'),
        1,
        4,
        'Skvělá nabídka! Klikněte na [Získat letenku nyní](http://letenky-super.example/deal) a získejte letenku za bezkonkurenční akční cenu. Nabídka končí už za 2 hodiny.',
        jsonb_build_object('sender', 'promo@letenky-super.example', 'subject',
                           'Speciální nabídka: letenky od 199 € - platí jen 2 hodiny!'),
        'Tento typ zpráv používá naléhavost a podezřelé ''příliš dobré'' tvrzení. Nabídka s extrémně nízkou cenou je častým trikem pro přilákání kliknutí. Před rezervací ověřte důvěryhodnost prodejce a platební formuláře.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'URGENT'),
        1,
        10,
        'Vážený uživateli,  zaznamenali jsme podezřelou platbu z vašeho účtu PayPal. Pro zablokování transakce a obnovení přístupu klikněte prosím na následující odkaz a ověřte svou identitu: [https://www.paypal.com/signin](http://paypal-secure.verify-example.com/login). Pokud jste tuto transakci neprovedli, jedná se o podvod a musíte jednat okamžitě.  Děkujeme, PayPal Security Team',
        jsonb_build_object('sender', 'alerts@paypal-security.example', 'subject',
                           'Ověřte platbu - podezřelá aktivita na vašem účtu'),
        'Odkaz a doména neodpovídají oficiální doméně PayPal (obsahují dodatečné části a podezřelou doménu). Žádost o okamžité přihlášení a ověření je typický trik pro získání přihlašovacích údajů (fake login).'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'LOTTERY'),
        1,
        8,
        'Ahoj,  na váš Steam účet vám byly přiděleny **bonusové kredity**. Pro jejich aktivaci klikněte zde: [Aktivovat kredity](http://steam-rewards-confirmation.com/bonus).  Děkujeme, Steam Team',
        jsonb_build_object('sender', 'support@steam-rewards.com', 'subject',
                           'Získali jste bonusové kredity – potvrďte převzetí'),
        'Doména neodpovídá skutečné službě Steam (použita podvodná doména). Tlačí na rychlou akci.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Zdravíme hráče!  Připravili jsme pro vás přehled nových titulů pro únor. Také jsme aktualizovali sekci předobjednávek: [Prozkoumat novinky](https://www.xzone.cz/novinky).  Xzone tým',
        jsonb_build_object('sender', 'newsletter@xzone.cz', 'subject', 'Novinky ze světa her – únorová edice'),
        'Běžný informační newsletter. Odkaz vede na oficiální doménu e-shopu.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'FAKE_DOC'),
        1,
        9,
        'Dobrý den,  objednávka z vašeho účtu byla **pozastavena** kvůli chybě v platbě. Pro pokračování prosím potvrďte údaje: [Potvrdit platbu](http://eshop-verify-payment.info/confirm).  Tým podpory',
        jsonb_build_object('sender', 'billing@eshop-prodejnalevne.cz', 'subject', 'Vaše objednávka byla pozastavena'),
        'Vyžaduje zadání platebních údajů přes nedůvěryhodnou URL. Neexistující nebo podezřelá doména.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        2,
        'Dobrý den,  u vaší objednávky došlo k aktualizaci doručovacího času. Detaily naleznete zde: [Stav objednávky](https://www.alza.cz/mojeobjednavky).  Děkujeme za nákup!',
        jsonb_build_object('sender', 'info@alza.cz', 'subject', 'Doručení zboží – aktualizace termínu'),
        'Standardní notifikace z e-shopu, odkazy směřují na oficiální doménu.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'CRED_THEFT'),
        1,
        10,
        'Dobrý den,  tento e-mail vyžaduje ověření vašeho účtu, jinak budete odstraněni z klubu. K ověření použijte tento odkaz: [Ověřit účet](http://rcmodel-verification.net/secure).  RC Model Club',
        jsonb_build_object('sender', 'rcmodely-support@secure-check.com', 'subject',
                           'Nutná aktualizace účtu v RC Model Clubu'),
        'Použita generická a nedůvěryhodná doména, nátlak na okamžité ověření účtu.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Ahoj modeláři,  naskladnili jsme nové modely letadel a příslušenství. Podívej se na ně zde: [Novinky](https://www.modelplac.cz/novinky).  S pozdravem, ModelPlác',
        jsonb_build_object('sender', 'info@modelplac.cz', 'subject', 'Nové modely letadel – skladem!'),
        'Legitimní newsletter s odkazem na oficiální doménu.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'SPEAR_PHISH'),
        1,
        8,
        'Dobrý den,  ve vaší poslední objednávce jsme zaznamenali problém. Klikněte sem a přihlaste se, abyste chybu odstranili: [Vyřešit problém](http://garden-fix-account.org/login).  Děkujeme.',
        jsonb_build_object('sender', 'garden-support@update-panel.com', 'subject',
                           'Problém s objednávkou zahradního vybavení'),
        'Podvodná URL a vyžadování přihlášení mimo skutečný e-shop.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        2,
        'Dobrý den,  připravili jsme pro vás slevu na zahradní nářadí. Více zde: [Zobrazit nabídku](https://www.zahradnictvi-zelena.cz/akce).  Přejeme krásný den!',
        jsonb_build_object('sender', 'info@zahradnictvi-zelena.cz', 'subject',
                           'Sleva na zahradní nářadí – jen tento týden'),
        'Běžný marketingový e-mail s odkazem na oficiální doménu firmy.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'CRED_THEFT'),
        1,
        7,
        'Dobrý den,  z bezpečnostních důvodů bylo vaše členství dočasně pozastaveno. Pro pokračování prosím aktualizujte údaje: [Aktualizovat účet](http://fitness-members-update.info/login).  Děkujeme.',
        jsonb_build_object('sender', 'support@fitness-center-security.com', 'subject',
                           'Pozastavené členství – nutná aktualizace údajů'),
        'Nátlak na aktualizaci účtu přes podezřelý odkaz a nedůvěryhodná doména.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Ahoj sportovče,  dokončili jste registraci na náš běžecký závod. Přikládáme doplnění k trati a mapu: [Informace o závodu](https://www.decathlon.cz/zavod-info).  Těšíme se na vás!',
        jsonb_build_object('sender', 'info@decathlon.cz', 'subject',
                           'Potvrzení registrace do závodu – doplňující informace'),
        'Legitimní potvrzení registrace, odkaz vede na oficiální doménu.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Ahoj všem,  zveme vás na společnou firemní snídani, která se uskuteční tento pátek od 9:00 v kuchyňce. Těšit se můžete na čerstvé pečivo a kávu.  Budeme se těšit, HR oddělení',
        jsonb_build_object('sender', 'hr@nase-firma.cz', 'subject', 'Pozvánka na firemní snídaně'),
        'Standardní interní komunikace. Odesílatel je z interní domény, e-mail nevyžaduje žádnou akci, neobsahuje podezřelé odkazy ani přílohy.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'URGENT'),
        1,
        6,
        'Vážený zákazníku,  kurýr se pokusil doručit vaši zásilku, ale nebyl úspěšný z důvodu nezaplaceného cla (35 Kč). Pro opětovné doručení prosím [uhradte poplatek zde](http://dhl-payment-gateway.xyz/pay). Pokud neuhradíte do 24 hodin, balík bude vrácen odesílateli.',
        jsonb_build_object('sender', 'delivery@dhl-track-package.xyz', 'subject', 'Váš balík nemohl být doručen'),
        'Klasický smishing/phishing zaměřený na doručovací služby. Doména ''dhl-track-package.xyz'' není oficiální web DHL. Částka je záměrně malá, aby ji lidé zaplatili bez přemýšlení, ale cílem je získat údaje z karty.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Vážený zákazníku,  vaše vyúčtování za minulý měsíc je připraveno. Částka k úhradě činí 599 Kč a bude automaticky stržena inkasem dne 15.11. Podrobný výpis najdete ve své samoobsluze nebo v přiloženém PDF (zaheslováno vaším rodným číslem).',
        jsonb_build_object('sender', 'fakturace@telefonni-operator.cz', 'subject',
                           'Vyúčtování služeb za období 10/2024'),
        'Legitimní e-mail od operátora. Odesílatel odpovídá oficiální doméně. Odkaz na samoobsluhu chybí (což je bezpečnější), nebo by vedl na oficiální web. Informace o inkasu naznačuje, že není nutná okamžitá manuální akce.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'SPEAR_PHISH'),
        1,
        8,
        'Ahoj, jsem momentálně na schůzce s klienty a nemůžu telefonovat. Potřebuji, abys pro mě rychle zařídil nákup dárkových karet Apple v hodnotě 5000 Kč pro partnery. Pošli mi kódy obratem, peníze ti proplatím hned zítra. Je to důležité.  Jan Novák CEO',
        jsonb_build_object('sender', 'reditel.jan.novak@gmail.com', 'subject', 'Prosba - urgentní'),
        'CEO Fraud (ředitelský podvod). Útočník se vydává za nadřízeného, ale píše z freemailu (gmail.com), nikoliv z firemní adresy. Vytváří tlak a žádá o nestandardní finanční transakci (nákup dárkových karet).'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Zaznamenali jsme nové přihlášení k vašemu účtu ''uzivatel@firma.cz'' ze zařízení ''Windows PC'' v lokalitě Brno. Pokud jste to byli vy, můžete tento e-mail ignorovat. Pokud ne, zkontrolujte svou aktivitu na account.microsoft.com.',
        jsonb_build_object('sender', 'no-reply@microsoft.com', 'subject', 'Bezpečnostní upozornění k účtu Microsoft'),
        'Legitimní bezpečnostní notifikace. E-mail neobsahuje tlačítka ''Klikněte zde pro zrušení'', která jsou typická pro phishing. Doména odesílatele je správná. Vyzývá k opatrnosti, ne k panice.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'FAKE_DOC'),
        1,
        10,
        'Dobrý den,  uživatel HR (hr@nase-firma.cz) s vámi sdílí zabezpečený dokument na OneDrive.  [Otevřít dokument](http://sharepoint-login.auth-secure.net/login)  K zobrazení souboru je nutné se přihlásit vaším pracovním e-mailem.',
        jsonb_build_object('sender', 'system@sharepoint-files-secure.net', 'subject',
                           'Kolega s vámi sdílí dokument: ''Plán_Bonusů_2024.xlsx'''),
        'Sofistikovaný útok na přihlašovací údaje (Credential Harvesting). Odkaz vede na podvodnou stránku imitující přihlášení do Microsoft 365/SharePointu. Doména odkazu je zcela cizí (''auth-secure.net'').'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Ahoj filmoví fanoušci,  přinášíme přehled filmů na tento víkend. Nenechte si ujít premiéru nového akčního trháku. Vstupenky zakoupíte online na našem webu nebo na pokladně.  Pokud už nechcete dostávat tyto zprávy, můžete se odhlásit.',
        jsonb_build_object('sender', 'newsletter@kino-svet.cz', 'subject', 'Program na tento víkend: Premiéry!'),
        'Běžný marketingový e-mail. Obsahuje odkaz na odhlášení (unsubscribe), což je zákonná povinnost. Nežádá žádné citlivé údaje.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'CRED_THEFT'),
        1,
        10,
        'Vážený kliente,  z důvodu přechodu na nový bezpečnostní standard přestane vaše aplikace zítra fungovat. Pro zachování přístupu k účtu je nutné [aktivovat nový klíč](http://mojebanka.aktualizace-app.eu/activator.exe) a nainstalovat aktualizaci do PC nebo mobilu.',
        jsonb_build_object('sender', 'podpora@moje-banka-servis.eu', 'subject',
                           'Důležité: Vaše mobilní aplikace vyprší'),
        'Velmi nebezpečný e-mail. Nabádá ke stažení spustitelného souboru (.exe) nebo aplikace z neoficiálního zdroje, což téměř jistě povede k instalaci malwaru/viru. Banky nikdy neposílají aktualizace e-mailem jako soubor.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Dobrý den,  děkujeme za vaši rezervaci. Těšíme se na vás 12.12. v 18:00. Pokud potřebujete rezervaci upravit, volejte na recepci +420 222 333 444.  S pozdravem, Recepce',
        jsonb_build_object('sender', 'rezervace@hotel-praha.cz', 'subject', 'Potvrzení vaší rezervace - 12.12.2024'),
        'Legitimní potvrzení služby. Obsahuje konkrétní, ale nezávadné údaje. Kontaktní údaje směřují na telefon, nikoliv na podvodný formulář.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'CRED_THEFT'),
        1,
        9,
        'Upozornění systému: Vaše heslo vypršelo před 3 dny. Váš účet bude zablokován, pokud heslo neobnovíte okamžitě. Ponechte si stejné heslo nebo zadejte nové zde: [Obnovit heslo](http://it-support-portal.com.shady-link.net/reset).',
        jsonb_build_object('sender', 'admin@it-support-portal.com', 'subject', 'Vaše heslo expirovalo'),
        'Klasický trik s expirací hesla. Odkaz vypadá na první pohled důvěryhodně (''it-support-portal.com''), ale ve skutečnosti je to jen subdoména na ''shady-link.net''. Vytváří stres hrozbou zablokování.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'CRED_THEFT'),
        1,
        10,
        'Dobrý den,  upozorňujeme vás, že platnost eReceptu na váš lék brzy vyprší. Pro prodloužení klikněte zde: [Prodloužit eRecept](http://erecept-prodlouzeni.example/verify).  Pokud recept neprodloužíte, nebude možné lék vydat.',
        jsonb_build_object('sender', 'eRecept@zdravotniportal-info.com', 'subject',
                           'Váš elektronický recept vyprší za 24 hodin'),
        'Zdravotnictví nikdy nevyžaduje prodlužování eReceptu přes externí odkazy. Doména není oficiální. Kombinace falešné URL a urgentní hrozby.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'URGENT'),
        1,
        10,
        'Vážený uživateli,  obdrželi jste novou datovou zprávu. Pro přihlášení použijte bezpečný přístup: [Přihlásit se do datové schránky](http://datovka-login.example/auth).  Zpráva bude automaticky smazána po 48 hodinách.',
        jsonb_build_object('sender', 'urad@mojedatovkaservis.com', 'subject',
                           'Zpráva ve vaší datové schránce: vyzvednutí nutné'),
        'Útočníci často napodobují datové schránky. Doména nemá nic společného s gov.cz. Sběr přihlašovacích údajů + urgence.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'FAKE_URL'),
        1,
        9,
        'Dobrý den,  v systému evidujeme doplatek 125 Kč. Uhradit ho můžete zde: [Zaplatit doplatek](http://pojistovna-doplatek.example/payment).  Děkujeme.',
        jsonb_build_object('sender', 'info@vseobecnazdrpoj.cz', 'subject', 'Nadstandardní doplatek za vyšetření'),
        'Falešná faktura z pojišťovny – doména je neexistující, pojišťovny neposílají platební odkazy e-mailem.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'URGENT'),
        1,
        8,
        'Dobrý den,  k doručení vaší zásilky je potřeba doplatit 19 Kč. Zaplaťte zde: [Uhradit doplatek](http://moje-zasilka.example/pay).  Bez úhrady nebude možné zásilku vydat.',
        jsonb_build_object('sender', 'info@prepravce-doplatek.cz', 'subject', 'Zásilka č. 4789 – nutný doplatek 19 Kč'),
        'Nízko částkové doplatky jsou pověstné mezi seniorskými podvody. Nejde o oficiální dopravce. Falešná URL + urgentní akce.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'FAKE_URL'),
        1,
        8,
        'Dobrý den,  v systému nevidíme platnou autorizaci vaší satelitní karty. Pokračujte zde pro ověření: [Aktivovat kartu](http://tv-karta.example/activate).  Jinak může dojít k přerušení příjmu.',
        jsonb_build_object('sender', 'servis@televizniset-top.cz', 'subject', 'Vaše satelitní karta bude deaktivována'),
        'Podvodníci využívají strach ze ztráty TV signálu. Doména je falešná, vyžaduje přihlášení – sběr přihlašovacích údajů.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'URGENT'),
        1,
        10,
        'Babi, prosím, stala se mi nepříjemnost a potřebuju půjčit trochu peněz. Tady můžeš poslat částku: [Poslat peníze](http://rychla-pomoc.example/send).  Ozvu se hned, jak budu moct.',
        jsonb_build_object('sender', 'vnuk.pavel@rodina-alert.info', 'subject', 'Babí, potřebuju rychle poslat peníze'),
        'Personalizovaný útok typu ''vnuk volá o pomoc''. Emoční manipulace, falešná doména, urgentní sociální inženýrství.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'FAKE_URL'),
        1,
        9,
        'Dobrý den,  poplatek za komunální odpad je nutné uhradit ještě dnes. Úhradu proveďte: [Zaplatit poplatek](http://obec-poplatek.example/pay).  Děkujeme.',
        jsonb_build_object('sender', 'info@obecni-urad-oznameni.com', 'subject', 'Poplatek za odpad – poslední výzva'),
        'Obecní úřady nemají generické .com domény. Jde o falešnou fakturu + urgentní výhružku.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Vážený pane / paní,  rádi vás pozveme na seminář o bezpečnosti seniorů. Prosíme o potvrzení účasti odpovědí na tento e-mail.  Těšíme se na vás.',
        jsonb_build_object('sender', 'info@seniorpomoc.cz', 'subject', 'Pozvánka na bezplatný seminář'),
        'Legitimně působící zpráva bez odkazů a bez urgentního tlaku. Žádost o jednoduchou odpověď je obvykle bezpečná.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'URGENT'),
        1,
        10,
        'Vážený klient,  kvůli podezřelé aktivitě musíme ověřit vaši platební kartu. Proveďte prosím bezpečné ověření zde: [Ověřit kartu](http://bezpecna-karta.example/verify).  Jinak bude karta dočasně blokována.',
        jsonb_build_object('sender', 'bankovni.sluzba@ucet-bezpecne.com', 'subject', 'Nutné ověření platební karty'),
        'Kombinace falešného loginu, získávání citlivých údajů a urgence. Banky nikdy neposílají ověřovací odkazy tímto způsobem.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'FAKE_URL'),
        1,
        9,
        'Dobrý den,  automaticky jsme detekovali potenciální přetížení vaší elektrické sítě. Doporučujeme provést online kontrolu: [Zkontrolovat síť](http://elektricka-kontrola.example/check).  Prevence chrání před požárem.',
        jsonb_build_object('sender', 'hlidac-elektro@domacnost-servis.cz', 'subject',
                           'Hrozí přetížení domácí sítě – nutná kontrola'),
        'Podvod využívající strach o bezpečí domácnosti. Falešná URL, psychologický nátlak, neexistující služba.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'CRED_THEFT'),
        1,
        10,
        'Vážený zákazníku,   zaznamenali jsme podezřelé přihlášení k vašemu účtu. Pro obnovení přístupu prosím ověřte svou totožnost zde: [Ověřit účet](http://moje-banka-verify.com/login). Pokud akci neprovedete do 24 hodin, bude účet uzamčen.',
        jsonb_build_object('sender', 'bezpecnost@moje-banka-verifikace.com', 'subject',
                           'DŮLEŽITÉ: Váš účet byl dočasně omezen'),
        'Podezřelá doména, výzva k okamžitému ověření a hrozba uzamčení účtu. Typické znaky phishingu.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'URGENT'),
        1,
        8,
        'Dobrý den,  dokument k faktuře naleznete zde: [Zobrazit fakturu](http://secure-faktura-pay.com/doc). Pokud nebude uhrazena do dnešního konce dne, dojde k omezení služeb.',
        jsonb_build_object('sender', 'billing@ucetni-portal-pay.net', 'subject',
                           'Faktura 2025/118 – vyžaduje okamžitou úhradu'),
        'Odkaz vede na neznámou doménu a e-mail vytváří nepřirozený tlak. Žádná legitimní služba nevyžaduje platbu přes neověřený odkaz.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'URGENT'),
        1,
        9,
        'Vážený uživateli,  z důvodu nových bezpečnostních opatření je nutné resetovat vaše heslo. Pro pokračování klikněte zde: [Resetovat heslo](http://microsoft-security-reset.example/verify).',
        jsonb_build_object('sender', 'noreply@m1crosoft-security.com', 'subject',
                           'Vyžaduje se reset vašeho Microsoft hesla'),
        'Falešná doména (m1crosoft), žádost o rychlou akci a odkaz na podezřelou URL.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'SPEAR_PHISH'),
        1,
        6,
        'Vážený zákazníku,  pro doručení zásilky uhraďte doplatek 49 Kč zde: [Uhradit doplatek](http://posta-doruceni-cz.net/pay). Pokud nebude doplatek uhrazen do 48 hodin, zásilka bude vrácena odesílateli.',
        jsonb_build_object('sender', 'ceskaposta@delivery-cz.net', 'subject',
                           'Doplatek 49 Kč – zásilka nemohla být doručena'),
        'Typický phishing: falešná doména, malá částka, tlak na rychlou akci.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'URGENT'),
        1,
        7,
        'Dobrý den,  tento unikátní poukaz lze aktivovat pouze dnes. Potvrďte prosím své údaje zde: [Aktivovat poukaz](http://reward-claim-win.com/activate).',
        jsonb_build_object('sender', 'vyherce@promo-rewards-win.com', 'subject',
                           'Gratulujeme! Vyhrál jste poukaz 5 000 Kč'),
        'Extrémně lákavá nabídka, agresivní časový limit a podezřelá doména jsou klasické rysy podvodu.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Dobrý den,  srdečně vás zveme na interní školení v úterý v 10:00. Přihlásit se můžete [zde](https://intranet.firma.cz/skoleni).',
        jsonb_build_object('sender', 'hr@firma.cz', 'subject', 'Pozvánka na školení kybernetické bezpečnosti'),
        'Oficiální firemní doména, žádné požadavky na přihlašovací údaje nebo platby.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Dobrý den,  potvrzujeme přijetí vaší objednávky. Stav objednávky můžete sledovat [ve svém účtu](https://www.elektro-shop.cz/muj-ucet).',
        jsonb_build_object('sender', 'objednavky@elektro-shop.cz', 'subject', 'Potvrzení objednávky č. 2025-4821'),
        'Standardní potvrzení objednávky z důvěryhodné domény. Žádné podezřelé požadavky.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Ahoj všichni,  zítra proběhne plánovaná odstávka VPN. Během této doby nebude možné se připojit. Nevyžaduje se žádná akce.',
        jsonb_build_object('sender', 'it-support@firma.cz', 'subject', 'Plánovaná odstávka VPN – středa 19:00–20:00'),
        'Běžné interní oznámení bez odkazů a bez nátlaku.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Vážení rodiče,  třídní schůzky proběhnou příští čtvrtek v 17:00. Prosíme o potvrzení účasti odpovědí na tento e-mail.',
        jsonb_build_object('sender', 'info@zakladni-skola.cz', 'subject', 'Informace o třídních schůzkách'),
        'Typická komunikace školy, bez podezřelých odkazů a bez požadavků na platby.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Vážený uživateli,  dne 23. 3. proběhne údržba služby CloudDrive mezi 01:00 a 03:00. Informace o stavu najdete [na stránce stavu služby](https://status.clouddrive.com).',
        jsonb_build_object('sender', 'noreply@clouddrive.com', 'subject', 'Údržba služby CloudDrive – 23. 3.'),
        'Legitimní servisní oznámení. Odkaz vede na oficiální doménu poskytovatele.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'FAKE_URL'),
        1,
        9,
        'Dobrý den,  v příloze zasíláme fakturu č. 2025-0892 za dodané kancelářské potřeby. Částka 4 850 Kč je splatná ještě dnes. Pro rychlou úhradu použijte tento odkaz: [Zaplatit fakturu](http://faktura-platba-rychle.example/pay).  Děkujeme za spolupráci.',
        jsonb_build_object('sender', 'fakturace@dodavatel-kancelar.net', 'subject',
                           'Faktura za kancelářské potřeby – splatnost DNES'),
        'Falešná faktura s nátlakem na okamžitou platbu. Doména odkazu je podezřelá a neodpovídá žádnému známému dodavateli. Vždy ověřte faktury přímo u dodavatele.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'FAKE_URL'),
        1,
        8,
        'Gratulujeme!  Vaše e-mailová adresa byla náhodně vylosována v naší mezinárodní loterii. Vyhráli jste 50 000 Kč! Pro převzetí výhry prosím vyplňte formulář: [Nárokovat výhru](http://loterie-vyzvedni.example/claim).  Výhra propadá za 48 hodin.',
        jsonb_build_object('sender', 'loterie-vyhry@super-loterie.info', 'subject',
                           'Byli jste vylosováni! Výhra 50 000 Kč čeká'),
        'Klasický loterijní podvod. Uživatel se žádné loterie neúčastnil. Cílem je získat osobní údaje nebo platbu za ''poplatky'' spojené s výhrou.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'URGENT'),
        1,
        9,
        'Ahoj,  omlouvám se, že tě ruším, ale mám problém. Jsem na služební cestě a ztratil jsem peněženku. Potřeboval bych, abys mi poslal/a 3000 Kč přes tento odkaz: [Poslat peníze](http://pomoc-kolegovi.example/send). Vrátím ti to hned v pondělí.  Díky moc, Martin',
        jsonb_build_object('sender', 'kolega.martin@firma-ext.com', 'subject', 'Urgentní – potřebuji tvou pomoc'),
        'Personalizovaný útok vydávající se za kolegu v nouzi. E-mail přichází z podezřelé domény (firma-ext.com místo firemní domény). Vždy ověřte identitu jiným kanálem.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'FAKE_URL'),
        1,
        8,
        'Vážený zákazníku,  evidujeme u vás nezaplacenou fakturu č. FA-2025-445 ve výši 2 340 Kč. Pokud nebude uhrazena do konce dne, bude účtováno penále. Fakturu a platební údaje naleznete zde: [Zobrazit fakturu](http://faktury-splatnost.example/doc).  S pozdravem, Účtárna',
        jsonb_build_object('sender', 'uctarna@nase-spolecnost-faktury.com', 'subject',
                           'Nezaplacená faktura – hrozí penále'),
        'Falešná faktura s hrozbou penále pro vytvoření tlaku. Podezřelá doména a obecný text bez konkrétních údajů o společnosti.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'FAKE_DOC'),
        1,
        9,
        'Dobrý den,  byli jste vybráni jako výherce naší soutěže o iPhone 15 Pro! Pro dokončení registrace a odeslání výhry prosím uhraďte manipulační poplatek 299 Kč: [Dokončit registraci](http://iphone-vyhra-claim.example/pay).  Nabídka platí pouze 24 hodin.',
        jsonb_build_object('sender', 'soutez@elektronika-vyhraj.net', 'subject', 'Výherce soutěže o iPhone 15 Pro!'),
        'Soutěžní podvod vyžadující platbu za ''manipulační poplatek''. Legitimní soutěže nikdy nevyžadují platbu od výherce. Kombinace urgence a lákavé nabídky.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Dobrý den,  potvrzujeme Váš termín zkoušky svatebních šatů na sobotu 18. ledna v 10:00. V případě potřeby změny termínu nás prosím kontaktujte na telefonu +420 777 123 456.  Těšíme se na Vás, Svatební salon Elegant',
        jsonb_build_object('sender', 'info@svatebni-salon-elegant.cz', 'subject', 'Potvrzení termínu zkoušky šatů'),
        'Legitimní potvrzení termínu ze svatebního salonu. Žádné podezřelé odkazy, kontakt pouze přes telefon.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Milí kávomilci,  do naší nabídky jsme zařadili limitovanou edici etiopské kávy z oblasti Yirgacheffe. Přijďte ochutnat do naší kavárny nebo objednejte online na [našem e-shopu](https://www.kavarna-zrnko.cz/eshop).  Těšíme se na vás!',
        jsonb_build_object('sender', 'newsletter@kavarna-zrnko.cz', 'subject', 'Nová limitovaná edice – Etiopská káva'),
        'Běžný newsletter z kavárny. Odkaz vede na oficiální doménu, žádné podezřelé požadavky.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Ahoj,  připomínáme ti, že zítra v 18:00 začíná tvůj kurz lezení pro začátečníky. Nezapomeň si vzít pohodlné oblečení a přezůvky. Lezečky ti zapůjčíme na místě.  V případě dotazů volej na +420 602 345 678.  Tým Boulder Centra',
        jsonb_build_object('sender', 'rezervace@lezecke-centrum-boulder.cz', 'subject',
                           'Připomínka kurzu lezení pro začátečníky'),
        'Legitimní připomínka kurzu z lezeckého centra. Osobní tón, konkrétní informace, kontakt přes telefon.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Ahoj pinballisté!  Zveme vás na první letošní turnaj v našem klubu. Startujeme 25. ledna od 14:00. Registrace probíhá na místě, startovné je 150 Kč. Více info na [našem webu](https://www.pinball-klub-praha.cz/turnaje).  Těšíme se!',
        jsonb_build_object('sender', 'turnaj@pinball-klub-praha.cz', 'subject',
                           'Pozvánka na pinballový turnaj – 25. ledna'),
        'Legitimní pozvánka na turnaj od pinballového klubu. Odkaz vede na oficiální web, žádné podezřelé požadavky.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Dobrý den,  v rámci našeho filmového klubu promítáme tento čtvrtek kultní film Pulp Fiction (1994). Začínáme ve 20:00, vstupenky zakoupíte na pokladně nebo [online zde](https://www.kino-lucerna.cz/vstupenky).  Přejeme příjemný filmový zážitek!',
        jsonb_build_object('sender', 'program@kino-lucerna.cz', 'subject',
                           'Filmový klub – tento čtvrtek: Pulp Fiction'),
        'Běžný informační e-mail z kina. Odkaz vede na oficiální doménu, standardní marketingová komunikace.'),
       (1,
        true,
        (SELECT id FROM phishing_categories WHERE tag = 'URGENT'),
        1,
        9,
        'Dobrý den,  byli jste vybráni jako výherce naší soutěže O2 o Samsung Galaxy S24! Pro dokončení převzetí výhry prosím ověřte svou identitu a uhraďte poštovné 149 Kč: [Převzít výhru](http://o2-soutez-vyhra.example/claim).  Výhra bude rezervována pouze 24 hodin.',
        jsonb_build_object('sender', 'vyhry@mobilni-soutez-cz.net', 'subject', 'Gratulujeme k výhře v soutěži O2!'),
        'Podvodný e-mail vydávající se za operátora O2. Doména neodpovídá oficiální doméně O2, vyžaduje platbu za ''poštovné'' a vytváří umělou urgenci. Legitimní soutěže nikdy nevyžadují platbu od výherce.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Milí snoubenci,  v příloze zasílám finální harmonogram vaší svatby na 15. června. Prosím o kontrolu časů a potvrzení, že je vše v pořádku. V případě jakýchkoli změn mě kontaktujte na telefonu +420 603 456 789.  S láskou, Petra – Vaše svatební koordinátorka',
        jsonb_build_object('sender', 'koordinator@svatby-na-klic.cz', 'subject',
                           'Harmonogram vaší svatby – finální verze'),
        'Legitimní komunikace od svatebního koordinátora. Osobní tón, konkrétní datum, kontakt přes telefon, žádné podezřelé odkazy.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Ahoj lezci!  V tomto týdnu jsme přestavěli žlutý a oranžový sektor. Přijďte si vyzkoušet 15 nových boulder problémů od 4a do 7b. Otevírací doba zůstává beze změn.  Těšíme se na vás, Tým Smíchoff',
        jsonb_build_object('sender', 'info@lezecka-stena-smichoff.cz', 'subject', 'Nové boulder problémy – leden 2025'),
        'Standardní informační e-mail z lezecké stěny. Žádné odkazy ani požadavky na akci, pouze informace o novinkách.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Dobrý den,  tento týden se v Kině Aero koná přehlídka českého filmu 90. let. Program a vstupenky najdete [na našem webu](https://www.kinoaero.cz/program).  Přejeme příjemné filmové zážitky!',
        jsonb_build_object('sender', 'program@kinoaero.cz', 'subject', 'Tento týden v Aeru: Přehlídka českého filmu'),
        'Legitimní newsletter z kina. Odkaz vede na oficiální doménu, standardní marketingová komunikace bez nátlaku.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Ahoj,  jen rychlá informace – páteční lekce kruhového tréninku se posouvá z 18:00 na 18:30 kvůli údržbě sálu. Omlouváme se za komplikace.  Uvidíme se v pátek! Tomáš',
        jsonb_build_object('sender', 'instruktor@fitko-zdravi.cz', 'subject', 'Změna času cvičení – tento pátek'),
        'Legitimní oznámení od instruktora fitness centra. Konkrétní informace o změně, osobní tón, žádné podezřelé požadavky.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Ahoj borci!  V sobotu 8. února pořádáme seminář s dvojnásobným mistrem světa v BJJ. Kapacita je omezená na 30 míst. Registrace na recepci nebo odpovědí na tento e-mail.  Cena: 800 Kč pro členy, 1200 Kč pro nečleny.  Roman, BJJ Akademie Praha',
        jsonb_build_object('sender', 'akademie@bjj-praha.cz', 'subject', 'Pozvánka na seminář s mistrem světa'),
        'Legitimní pozvánka na seminář od BJJ akademie. Konkrétní informace, standardní způsob registrace, žádné podezřelé odkazy.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Dobrý den,  vaše objednávka č. 2025-1847 (Etiopie Sidamo 500g, Brazílie Santos 250g) byla dnes odeslána. Zásilku můžete [sledovat zde](https://tracking.ppl.cz/123456).  Děkujeme za nákup! Pražírna u Karla',
        jsonb_build_object('sender', 'objednavky@prazirna-kava.cz', 'subject', 'Vaše objednávka byla odeslána'),
        'Legitimní potvrzení odeslání objednávky. Konkrétní číslo objednávky, odkaz na sledování zásilky u známého dopravce.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Dobrý den,  rádi vám oznamujeme, že Whey Protein Isolate čokoláda je opět skladem. Jako stálý zákazník máte nárok na 10% slevu s kódem LOYAL10.  Nakupujte [na našem e-shopu](https://www.fitness007.cz).  Váš Fitness007 tým',
        jsonb_build_object('sender', 'info@fitness007.cz', 'subject', 'Vaše oblíbené proteiny opět skladem!'),
        'Legitimní marketingový e-mail od známého e-shopu se suplementy. Odkaz vede na oficiální doménu, slevový kód je běžná praxe.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Ahoj,  na příští hodinu si prosím připravte popis svého oblíbeného filmu v angličtině (5-10 vět). Budeme procvičovat minulý čas a slovní zásobu.  Těším se v úterý! Mark',
        jsonb_build_object('sender', 'lektor@jazykova-skola-bridge.cz', 'subject',
                           'Domácí úkol na příští hodinu angličtiny'),
        'Legitimní e-mail od lektora angličtiny. Osobní tón, konkrétní úkol, žádné podezřelé odkazy ani požadavky.'),
       (1,
        false,
        (SELECT id FROM phishing_categories WHERE tag = 'LEGIT'),
        1,
        1,
        'Dobrý den,  potvrzujeme vaši registraci do kurzu Business English – úroveň B2. Kurz začíná 3. února v 18:00. Přístupové údaje do online učebny obdržíte den před zahájením.  S pozdravem, Jazyková škola Online',
        jsonb_build_object('sender', 'registrace@anglictina-online.cz', 'subject',
                           'Potvrzení registrace do kurzu Business English'),
        'Legitimní potvrzení registrace do jazykového kurzu. Konkrétní informace o kurzu, standardní komunikace bez podezřelých prvků.')
ON CONFLICT (platform_type_id, md5(content)) DO NOTHING;

-- Init Problems table
INSERT INTO problems (tag, description) VALUES
    ('fake-sender',           'Falešný odesílatel – adresa se neshoduje s doménou tvrzené organizace'),
    ('suspicious-url',        'Podezřelá URL – odkaz nevede na oficiální doménu organizace'),
    ('time-pressure',         'Umělý časový tlak – e-mail nutí k okamžité akci pod hrozbou'),
    ('sensitive-info-request','Žádost o citlivé údaje – e-mail žádá o heslo, číslo karty nebo osobní data'),
    ('typosquatting',         'Typosquatting – doména je záměrně podobná důvěryhodné stránce'),
    ('impersonation',         'Vydávání se za jinou osobu nebo organizaci'),
    ('fake-prize',            'Falešná výhra – tvrdí, že uživatel vyhrál v loterii nebo soutěži, o které nevěděl'),
    ('attachment-risk',       'Riziková příloha nebo škodlivý soubor nabízený ke stažení')
ON CONFLICT (tag) DO NOTHING;

-- Init Question-Problems
-- Q1: CRED_THEFT – mybank-support phishing
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%mybank-support.verify-login.example%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'time-pressure', 'sensitive-info-request')
ON CONFLICT DO NOTHING;

-- Q3: URGENT – fake invoice pay-now
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%pay-now.example/payment?id=78945%'
  AND p.tag IN ('time-pressure', 'suspicious-url', 'fake-sender')
ON CONFLICT DO NOTHING;

-- Q4: FAKE_URL – service terms update
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%notifications@sluzby.example.org%'
  AND p.tag IN ('suspicious-url', 'fake-sender')
ON CONFLICT DO NOTHING;

-- Q5: URGENT – password reset firma-corp
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%firma-corp.reset-password.example%'
  AND p.tag IN ('suspicious-url', 'typosquatting')
ON CONFLICT DO NOTHING;

-- Q7: SPEAR_PHISH – billing.dodavatel payment verification
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%billing.dodavatel.example/verify?token=XYZ%'
  AND p.tag IN ('suspicious-url', 'sensitive-info-request', 'fake-sender')
ON CONFLICT DO NOTHING;

-- Q9: FAKE_URL – letenky-super flight deal
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%letenky-super.example/deal%'
  AND p.tag IN ('suspicious-url', 'time-pressure', 'fake-sender')
ON CONFLICT DO NOTHING;

-- Q10: URGENT – fake PayPal security alert
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%paypal-secure.verify-example.com/login%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'typosquatting', 'time-pressure', 'sensitive-info-request')
ON CONFLICT DO NOTHING;

-- Q11: LOTTERY – fake Steam bonus credits
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%steam-rewards-confirmation.com/bonus%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'fake-prize', 'time-pressure')
ON CONFLICT DO NOTHING;

-- Q13: FAKE_DOC – eshop payment confirmation
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%eshop-verify-payment.info/confirm%'
  AND p.tag IN ('suspicious-url', 'fake-sender', 'sensitive-info-request')
ON CONFLICT DO NOTHING;

-- Q15: CRED_THEFT – RC Model Club account verification
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%rcmodel-verification.net/secure%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'time-pressure')
ON CONFLICT DO NOTHING;

-- Q17: SPEAR_PHISH – garden-fix account login
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%garden-fix-account.org/login%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'sensitive-info-request')
ON CONFLICT DO NOTHING;

-- Q19: CRED_THEFT – fitness membership suspension
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%fitness-members-update.info/login%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'time-pressure', 'sensitive-info-request')
ON CONFLICT DO NOTHING;

-- Q22: URGENT – fake DHL customs fee
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%dhl-payment-gateway.xyz/pay%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'time-pressure', 'sensitive-info-request', 'typosquatting')
ON CONFLICT DO NOTHING;

-- Q24: SPEAR_PHISH – CEO fraud (gmail)
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%reditel.jan.novak@gmail.com%'
  AND p.tag IN ('fake-sender', 'impersonation', 'time-pressure', 'sensitive-info-request')
ON CONFLICT DO NOTHING;

-- Q26: FAKE_DOC – fake SharePoint login
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%sharepoint-login.auth-secure.net/login%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'typosquatting', 'sensitive-info-request')
ON CONFLICT DO NOTHING;

-- Q28: CRED_THEFT – fake bank app update (malware .exe)
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%activator.exe%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'attachment-risk', 'time-pressure')
ON CONFLICT DO NOTHING;

-- Q30: CRED_THEFT – password expiry with subdomain trick
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%it-support-portal.com.shady-link.net%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'typosquatting', 'time-pressure')
ON CONFLICT DO NOTHING;

-- Q31: CRED_THEFT – fake eRecept expiry
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%erecept-prodlouzeni.example/verify%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'time-pressure')
ON CONFLICT DO NOTHING;

-- Q32: URGENT – fake data mailbox (datovka)
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%datovka-login.example/auth%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'time-pressure', 'sensitive-info-request')
ON CONFLICT DO NOTHING;

-- Q33: FAKE_URL – fake insurance surcharge
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%pojistovna-doplatek.example/payment%'
  AND p.tag IN ('fake-sender', 'suspicious-url')
ON CONFLICT DO NOTHING;

-- Q34: URGENT – fake courier small fee
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%moje-zasilka.example/pay%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'time-pressure')
ON CONFLICT DO NOTHING;

-- Q35: FAKE_URL – fake satellite card deactivation
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%tv-karta.example/activate%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'sensitive-info-request')
ON CONFLICT DO NOTHING;

-- Q36: URGENT – fake "grandson" money request
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%rychla-pomoc.example/send%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'time-pressure', 'impersonation')
ON CONFLICT DO NOTHING;

-- Q37: FAKE_URL – fake municipal waste fee
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%obec-poplatek.example/pay%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'time-pressure')
ON CONFLICT DO NOTHING;

-- Q39: URGENT – fake bank card verification
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%bezpecna-karta.example/verify%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'time-pressure', 'sensitive-info-request')
ON CONFLICT DO NOTHING;

-- Q40: FAKE_URL – fake electricity overload warning
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%elektricka-kontrola.example/check%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'time-pressure')
ON CONFLICT DO NOTHING;

-- Q41: CRED_THEFT – fake bank account locked
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%moje-banka-verify.com/login%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'time-pressure')
ON CONFLICT DO NOTHING;

-- Q42: URGENT – fake invoice end-of-day
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%secure-faktura-pay.com/doc%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'time-pressure')
ON CONFLICT DO NOTHING;

-- Q43: URGENT – typosquatted Microsoft (m1crosoft)
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%m1crosoft-security.com%'
  AND p.tag IN ('fake-sender', 'typosquatting', 'suspicious-url', 'time-pressure')
ON CONFLICT DO NOTHING;

-- Q44: SPEAR_PHISH – fake Czech Post delivery fee
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%posta-doruceni-cz.net/pay%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'time-pressure', 'impersonation')
ON CONFLICT DO NOTHING;

-- Q45: URGENT – fake voucher prize activation
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%reward-claim-win.com/activate%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'fake-prize', 'time-pressure')
ON CONFLICT DO NOTHING;

-- Q51: FAKE_URL – fake office supplies invoice
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%faktura-platba-rychle.example/pay%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'time-pressure')
ON CONFLICT DO NOTHING;

-- Q52: FAKE_URL – fake lottery win 50 000 Kč
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%loterie-vyzvedni.example/claim%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'fake-prize', 'time-pressure')
ON CONFLICT DO NOTHING;

-- Q53: URGENT – colleague impersonation money request
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%pomoc-kolegovi.example/send%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'impersonation', 'time-pressure')
ON CONFLICT DO NOTHING;

-- Q54: FAKE_URL – fake unpaid invoice with penalty
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%faktury-splatnost.example/doc%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'time-pressure')
ON CONFLICT DO NOTHING;

-- Q55: FAKE_DOC – fake iPhone prize with advance fee
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%iphone-vyhra-claim.example/pay%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'fake-prize', 'time-pressure')
ON CONFLICT DO NOTHING;

-- Q61: URGENT – fake O2 contest prize
INSERT INTO question_problems (question_id, problem_id)
SELECT q.id, p.id FROM questions q, problems p
WHERE q.content LIKE '%o2-soutez-vyhra.example/claim%'
  AND p.tag IN ('fake-sender', 'suspicious-url', 'impersonation', 'fake-prize', 'time-pressure')
ON CONFLICT DO NOTHING;