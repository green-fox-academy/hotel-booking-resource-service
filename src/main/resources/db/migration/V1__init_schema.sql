CREATE TABLE hearthbeat (
  "status" BOOLEAN NOT NULL, PRIMARY KEY ("status")
);
INSERT INTO hearthbeat VALUES (TRUE );

CREATE TABLE hotel (
  "id" BIGINT NOT NULL, PRIMARY KEY ("id"),
  "location" VARCHAR,
  "name" VARCHAR,
  "main_image_src" VARCHAR,
  "has_wifi" BOOLEAN,
  "has_parking" BOOLEAN,
  "has_pets" BOOLEAN,
  "has_restaurant" BOOLEAN,
  "has_bar" BOOLEAN,
  "has_swimming_pool" BOOLEAN,
  "has_air_conditioning" BOOLEAN,
  "has_gym" BOOLEAN,
  "meal_plan" VARCHAR,
  "stars" INT
);