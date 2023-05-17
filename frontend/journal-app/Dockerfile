FROM node:18-alpine as builder
WORKDIR /app
COPY package.json ./
COPY package-lock.json ./
COPY . /app/

RUN npm install --silent
RUN npm install react-scripts@4.0.3 -g --silent
RUN npm run build


# production environment
FROM nginx:latest

#COPY ./nginx/nginx.conf /etc/nginx/conf.d/nginx.conf

# Set working directory to nginx asset directory
WORKDIR /usr/share/nginx/html

# Remove default nginx static assets
RUN rm -rf ./*

# Copy static assets from builder stage
COPY --from=builder /app/build .

EXPOSE 80 
CMD ["nginx", "-g", "daemon off;"]