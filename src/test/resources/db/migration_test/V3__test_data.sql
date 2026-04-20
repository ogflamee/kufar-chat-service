INSERT INTO messages (id, sender_id, receiver_id, ad_id, text, created_at) VALUES
(100, 100, 1, 1, 'Test message 1', NOW()),
(101, 1, 100, 2, 'Reply to test', NOW()),
(102, 100, 2, 3, 'Another test message', NOW());