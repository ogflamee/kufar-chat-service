CREATE TABLE messages (
   id SERIAL PRIMARY KEY,
   sender_id INTEGER NOT NULL,
   receiver_id INTEGER  NOT NULL,
   ad_id INTEGER  NOT NULL,
   text VARCHAR(255) NOT NULL,
   created_at TIMESTAMP DEFAULT NOW()
);