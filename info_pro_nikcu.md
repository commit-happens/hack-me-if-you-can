1. Aktualizovala jsem pom.xml aby tam bylo PostgreSQL
2. Je treba nechat H2 pro testovani! Toto je mega dulezite :O Kdyz to smazeme tak testy nefunguji
3. Aktualizovala jsem application.yaml s konfiguraci PostgreSQL. Jsou tam dane promenne pro vyplneni. Skutecne hodnoty ma kazdy uzivatel u sebe .env
4. Mame taky application-test.yaml v test/ pro stestovaci ucely. Tam je konfigurace nastavena pro H2
5. Ohledne tech .env: Na produkci je sablona .env.template kterou si kazdy u sebe lokalne nakopiruje jako .env a tam si nastavi skutecne hodnoty (aktualne jsou stejne jako .env.template)
6. docker-compose.yaml obsahuje spousteni postgreSQL a pouziva pro pripojeni promenne ze souboru .env

Jak jsem spoustela postgreSQL u sebe (testovani ze to funguje)
1. Spustim si docker pomoci: `docker-compose up build --no-cahce` (to instaluje vse uplne od zacatky -hodi se kdyz se docker image nejak rozbije a chces ho cely vytvorit znovu) pokud uz existuje a chces ho jen spustit tak staci `docker-compose up -d` a pro vypnuti `docker-compose down -v`
2. Spustim si posgreSQL v dockeru: `docker exec -it hmiyc-postgres psql -U postgres -d hmiyc`
3. Muzu se ted divat co je v databazi: uzitecne prikazy
    1. `\dt` - vrati vsechny tabulky
    2. `\d player` - vrati informace o tabulce player
    3. `SELECT * FROM player;` - vrati vse v tabulce player (dulezity je strednik na konci, jinak to nevraci nic)
    4. `\q` - odhlasi te
4. Ze zacatku je to prazdne, ale muzes tam vlozit data pomoci curl prikazu
```
curl -X 'POST' \
  'http://localhost:8080/api/players' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "nickname": "nejaky_uzivatel",
  "score": 100
}'
```
5. Mela by jsi toho uzivatele pak videt i na http://localhost:8080/api/players
6. Pokud se podivas ha H2 databazi tak nebezi>: http://localhost:8080/h2-console/login.jsp?jsessionid=f91e667e117e5ef03d13d4d3b26296b0
3. A po prihlaseni do postgreSQL v dockeru a podivani se do tabulky player uvidis player
```
docker exec -it hmiyc-postgres psql -U postgres -d hmiyc
SELECT * FROM player;
```