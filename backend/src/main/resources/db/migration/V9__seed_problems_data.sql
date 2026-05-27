-- Seed data pro tabulku `problems` (všechny dostupné tagy bezpečnostních chyb)
INSERT INTO problems (tag, description) VALUES
    ('time-pressure', 'Uměle vytvářený tlak časovým limitem (urgování, hrozba zablokování nebo sankce).'),
    ('generic-greeting', 'Neosobní přístup (např. "Vážený zákazníku") místo personalizovaného jména u služeb, kde by jméno mělo být.'),
    ('grammar-errors', 'Špatná gramatika, strojový překlad, chybějící diakritika nebo nepřirozený slovosled.'),
    ('fake-html', 'Podvržené odkazy, nesrovnalost mezi textem odkazu a reálným cílem, falešná tlačítka.'),
    ('domain-spoof', 'Podezřelá nebo podvržená doména odesílatele, která napodobuje známou instituci.'),
    ('suspicious-attachment', 'Výzva ke stažení neočekávané nebo nebezpečné přílohy.'),
    ('fake-url', 'Podezřelý odkaz, neoficiální doména napodobující instituci, nebo použití zkracovače (bit.ly, tinyurl apod.).'),
    ('sender-spoof', 'Podezřelé telefonní číslo odesílatele (např. zahraniční předvolba pro českou službu) nebo podvržené textové ID odesílatele.')
ON CONFLICT (tag) DO NOTHING;

