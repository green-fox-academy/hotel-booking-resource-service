CREATE TABLE booking (
  "id" BIGINT NOT NULL, PRIMARY KEY ("id"),
  "guests" INTEGER,
  "start_date" VARCHAR,
  "end_date" VARCHAR,
  "created_at" VARCHAR,
  "description" VARCHAR,
  "hotel_id" BIGINT
);