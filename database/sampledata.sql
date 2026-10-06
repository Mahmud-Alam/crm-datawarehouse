INSERT INTO accounts (account_id, account_name, sector, year_established, revenue, employees, office_location, subsidiary_of) VALUES
(1, 'Acme Corporation',     'Technology',     1996, 1100.04,  2822, 'United States', NULL),
(2, 'Betasoloin',           'Medical',        1999,  251.41,   495, 'United States', NULL),
(3, 'Betatech',             'Medical',        1986,  647.18,  1185, 'Kenya',         NULL),
(4, 'Cancity',              'Retail',         2001,  718.62,  2448, 'United States', NULL),
(5, 'Isdom',                'Medical',        2002, 3178.24,  4540, 'United States', NULL);

INSERT INTO products (product_id, product_name, series, sales_price) VALUES
(1, 'GTX Basic',       'GTX',   550.00),
(2, 'GTX Pro',         'GTX',  4821.00),
(3, 'MG Special',      'MG',     55.00),
(4, 'MG Advanced',     'MG',   3393.00),
(5, 'GTX Plus Pro',    'GTX',  5482.00),
(6, 'GTX Plus Basic',  'GTX',  1096.00),
(7, 'GTK 500',         'GTK', 26768.00);

INSERT INTO sales_teams (sales_agent_id, sales_agent, manager, regional_office) VALUES
(1, 'Moses Frase',        'Dustin Brinkmann', 'Central'),
(2, 'Darcel Schlecht',    'Melvin Marxen',    'Central'),
(3, 'Anna Snelling',      'Dustin Brinkmann', 'Central'),
(4, 'Vicki Laflamme',     'Celia Rouche',     'West');

INSERT INTO opportunities (opportunity_id, sales_agent_id, product_id, account_id, deal_stage, engage_date, close_date, close_value) VALUES
('1C1I7A6R', 1, 6, 4, 'won', '2016-10-20', '2017-03-01', 1054.00),
('Z063OYW0', 2, 2, 5, 'won', '2016-10-25', '2017-03-11', 4514.00),
('7F2M9K3P', 3, 1, 1, 'lost','2017-01-15', '2017-04-20',  550.00),
('A8B3C4D5', 4, 5, 2, 'won', '2017-02-10', '2017-05-15', 5482.00);
