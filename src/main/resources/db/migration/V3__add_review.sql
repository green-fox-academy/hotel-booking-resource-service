CREATE TABLE review (
  "id" BIGINT NOT NULL, PRIMARY KEY ("id"),
  "created_at" VARCHAR,
  "description" VARCHAR,
  "rating" INTEGER,
  "hotel_id" BIGINT
);