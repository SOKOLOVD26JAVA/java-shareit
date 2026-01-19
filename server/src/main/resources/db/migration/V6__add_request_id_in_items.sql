ALTER TABLE items
ADD COLUMN IF NOT EXISTS request_id BIGINT;

ALTER TABLE items
ADD CONSTRAINT fk_items_request
FOREIGN KEY (request_id)
REFERENCES requests(id)