# ==================================
# 🏗️ STAGE 1: Build Stage
# Kompilace a buildování React aplikace
# ==================================
FROM node:20-alpine AS build

WORKDIR /app

# Kopírování package.json a package-lock.json pro cachování závislostí
COPY package*.json ./

# Instalace závislostí
RUN npm ci

# Kopírování zdrojového kódu
COPY . .

# Build produkční verze aplikace
RUN npm run build

# ==================================
# 🚀 STAGE 2: Runtime Stage
# Servírování aplikace přes nginx
# ==================================
FROM nginx:alpine

# Kopírování buildu z předchozího stage
COPY --from=build /app/dist /usr/share/nginx/html

# Kopírování vlastní nginx konfigurace pro React Router
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponování portu
EXPOSE 80

# Spuštění nginx
CMD ["nginx", "-g", "daemon off;"]
