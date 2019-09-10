-- Currently being addressed in https://github.com/jOOQ/jOOQ/issues/9131
-- Workaround: remove the b and single quotes

CREATE TABLE `example5` (
 `field` bit(1) NOT NULL DEFAULT b'0'
);
