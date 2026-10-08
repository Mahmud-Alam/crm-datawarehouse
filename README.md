# 🏪 CRM Data Warehouse

**A web-based data warehouse for B2B sales opportunities — extract, transform, load, then query.**

> A Java ETL pipeline that ingests CSV and XML datasets into a MySQL data warehouse, with a PHP query engine and a clean HTML front-end for running reports and analyses on CRM sales data.

**Live Demo:** _Not deployed — runs locally on XAMPP/Apache + MySQL_
**Source Code:** [github.com/Mahmud-Alam/crm-datawarehouse](https://github.com/Mahmud-Alam/crm-datawarehouse)

---

## 📖 About The Project

**CRM Data Warehouse** is a web-based data warehouse system built for managing and analysing B2B sales opportunities. The project integrates heterogeneous source data (CSV files for products and the sales pipeline, XML files for accounts and sales teams) into a single MySQL data warehouse, then exposes four pre-defined reports and analyses through a PHP middleware layer and a lightweight HTML front-end.

The system follows a classic four-tier architecture: raw data files at the bottom, a Java-based ETL pipeline that does the Extract-Transform-Load work, a MySQL data warehouse that stores the cleaned and integrated data, and an application layer made up of a PHP query engine (middleware) and an HTML front-end (browser). The Java ETL code is organised as a Maven project under the `crm` package, with five classes that each handle one concern: parsing CSV, parsing XML, transforming data, loading into MySQL, and orchestrating the whole pipeline.

The web interface gives a non-technical user four buttons that trigger the four required reports and analyses. The browser sends a request to `queryEngine.php`, which connects to the warehouse, runs the SQL, and returns the result as JSON. The front-end renders the JSON into an HTML table with money formatting and coloured deal-stage pills. The whole experience is intentionally simple — no authentication, no pagination, no JavaScript framework — because the focus of the project is the integration and ETL work, not the UI.

### 🎯 Purpose & Motivation

**The problem.** Modern enterprises rarely keep all their data in one place. Sales data lives in a CRM, account data in an ERP, product data in a catalogue, and team data in an HR system. Each system exports data in its own format — CSV, XML, JSON, fixed-width — and before any meaningful analysis can happen, all of that data needs to be pulled together into one consistent store. This is the classic Extract-Transform-Load problem, and it is what this project tackles head-on.

**The idea.** Build a small but realistic data warehouse for a fictitious computer hardware company. Take four source datasets in two different formats, clean and standardise them, load them into a normalised MySQL schema with proper foreign keys, and then expose the integrated data through a query engine that produces the reports and analyses a sales manager would actually want.

**The dataset.** The project uses the Maven Analytics _CRM Sales Opportunities_ dataset. It contains four entities — accounts (companies), products, sales teams, and sales opportunities — and comes in two formats: `accounts.xml` and `sales_teams.xml` for the hierarchical data, `products.csv` and `sales_pipeline.csv` for the tabular data. The data is not perfectly clean: the `sector` column in `accounts.xml` contains a typo (`technolgy` instead of `technology`) and inconsistent casing, which is exactly the kind of real-world messiness the transformation step is meant to handle.

**The learning.** This project was built as the final assessment for ICT914 Enterprise Systems Integration & Engineering. It forced us to think about every layer of an integration pipeline: schema design with surrogate keys, foreign key resolution when source data has no IDs, idempotent loading so re-runs do not duplicate data, and clean separation between the ETL, the database, and the application layers.

**The name.** A _data warehouse_ — the single source of truth that the rest of the business reports against.

---

## 🏗️ System Architecture

The application follows a four-tier architecture with clear separation between data sources, ETL, storage, and application layers.

```
┌─────────────────────────────────────────────────────────────────┐
│                        DATA SOURCE TIER                          │
│                                                                  │
│   Raw files supplied with the Maven Analytics dataset            │
│                                                                  │
│   • accounts.xml        (XML — companies, sector, revenue)       │
│   • sales_teams.xml     (XML — agents, managers, regions)        │
│   • products.csv        (CSV — product, series, sales_price)     │
│   • sales_pipeline.csv  (CSV — opportunities, deal_stage, value) │
└────────────────────────────┬────────────────────────────────────┘
                             │ Read by Java parsers
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                          ETL TIER                                │
│                                                                  │
│   Java 25 + Maven (package: crm)                                 │
│                                                                  │
│   Pipeline:                                                      │
│   XMLParser ──┐                                                  │
│   CSVParser ──┼──> DataTransformer ──> MySQLLoader ──> ETL_Main  │
│                                                                  │
│   • DOM parser for XML (javax.xml.parsers)                       │
│   • BufferedReader parser for CSV (quote-aware)                  │
│   • Clean missing values + standardise sector + normalise stage  │
│   • JDBC PreparedStatement batch inserts                         │
│   • Name-to-ID lookup maps for foreign key resolution            │
│   • JUnit 5 unit tests in src/test/java/crm                      │
└────────────────────────────┬────────────────────────────────────┘
                             │ JDBC (mysql-connector-j 8.x)
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                        STORAGE TIER                              │
│                                                                  │
│   MySQL 8.x — database: crm_warehouse                            │
│                                                                  │
│   4 tables:                                                      │
│   • accounts        (account_id PK, sector, revenue, ...)        │
│   • products        (product_id PK, product_name, series, ...)   │
│   • sales_teams     (sales_agent_id PK, sales_agent, region)     │
│   • opportunities   (opportunity_id PK, 3 FKs to above)          │
│                                                                  │
│   • AUTO_INCREMENT surrogate keys (source data has no IDs)       │
│   • Foreign key constraints enforce referential integrity        │
│   • revenue stored in millions of USD (per data dictionary)      │
└────────────────────────────┬────────────────────────────────────┘
                             │ mysqli (PHP)
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                      APPLICATION TIER                            │
│                                                                  │
│   Apache + PHP 7.4+ (XAMPP)                                      │
│                                                                  │
│   • queryEngine.php  — middleware, returns JSON                  │
│   • index.html       — static UI, fetch + render                 │
│   • css/style.css    — styling, deal-stage pills                 │
│                                                                  │
│   • 4 endpoints: products, opportunities, revenue_by_year,      │
│     opportunity_analysis                                         │
│   • Single-file middleware (switch on report param)              │
│   • Front-end renders JSON into HTML table                       │
└─────────────────────────────────────────────────────────────────┘
```

### Communication Flow

1. The browser loads `index.html` and renders four report buttons.
2. When the user clicks a button, JavaScript `fetch` sends a GET request to `queryEngine.php?report=<name>`.
3. The PHP script opens a `mysqli` connection to the `crm_warehouse` database.
4. A `switch` statement picks the SQL query corresponding to the report name.
5. The query runs against MySQL and the result rows are collected into a PHP array.
6. The script returns the array as a JSON document with the shape `{ label, count, rows }`.
7. The front-end parses the JSON and renders the rows into an HTML table, formatting money columns and colouring the `deal_stage` pills.

---

## ✨ Key Features

### 📊 Reports

- **Products Report** — every product in the catalogue, with series and sales price. Useful for sanity-checking that the ETL loaded the products correctly.
- **Sales Opportunities Report** — won and lost opportunities joined to their product, account, and sales agent. The `deal_stage` column is rendered as a coloured pill (green for won, red for lost) so the table is scannable at a glance.

### 📈 Analyses

- **Establishment Year Revenue Analysis** — total and average revenue grouped by the year each account was founded. Surfaces whether older companies consistently out-earn newer ones, or whether the relationship is noisier than that.
- **Sales Opportunity Analysis** — total and average `close_value` per product, ordered by total value descending. Tells the sales team which products are actually bringing in revenue versus which ones generate a lot of activity but close for small amounts.

### 🔄 ETL Pipeline

- **Dual-format ingestion** — DOM parser for XML, BufferedReader parser for CSV, both returning `List<Map<String,String>>` so downstream stages do not care about the source format.
- **Missing-value cleaning** — empty strings, `null`, and `n/a` are converted to real SQL `NULL` so aggregate functions behave correctly.
- **Sector standardisation** — fixes the `technolgy` typo and converts every sector value to Title Case (e.g. `medical` → `Medical`, `Food & Beverage` stays as-is).
- **Deal-stage normalisation** — the source CSV uses title case (`Won`, `Lost`, `Engaging`, `Prospecting`); the warehouse stores lowercase (`won`, `lost`, `engaging`, `prospecting`) to match the brief's terminology and keep `WHERE` clauses simple.
- **Foreign-key resolution** — because the source data has no numeric IDs, the loader builds name-to-ID lookup maps after the parent tables are loaded and walks through the opportunity rows to resolve `sales_agent`, `product`, and `account` names into their surrogate IDs.
- **Idempotent loading** — every `loadTable` call deletes existing rows first, so the pipeline can be re-run as many times as needed without producing duplicates.

### 🖥️ Web Interface

- **Four-button UI** — each button triggers one report or analysis. The active button is highlighted so the user always knows which report they are looking at.
- **Dynamic table rendering** — column headers come from the JSON keys, so adding or removing columns in the SQL does not require touching the front-end.
- **Money formatting** — `sales_price`, `close_value`, `total_revenue`, and similar columns are formatted with thousands separators and two decimals via `toLocaleString`.
- **Deal-stage pills** — `won`, `lost`, `engaging`, and `prospecting` each get a distinct background colour for quick visual scanning.
- **Responsive layout** — works on phones, tablets, and desktops without a CSS framework.

---

## 🛠️ Tech Stack

### ETL Pipeline

| Technology                                                 | Role                | Why it was chosen                                                                                              |
| --------------------------------------------------------- | ------------------- | -------------------------------------------------------------------------------------------------------------- |
| [Java](https://www.java.com) 25+                          | Language            | Required by the brief. Plain Java — no framework, no Spring Boot, because the ETL is a batch script, not a web app. |
| [Maven](https://maven.apache.org)                          | Build tool          | Standard Java build tool. Handles the `mysql-connector-j` dependency and the `src/main/java` + `src/test/java` layout. |
| [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) 8.x | JDBC driver | The official driver. Loaded as a Maven dependency so we never have to manage JARs by hand.                     |
| [JUnit 5](https://junit.org/junit5/)                       | Unit testing        | Standard testing framework for Java. Used for parser and transformer tests.                                   |
| Java DOM (`javax.xml.parsers`)                             | XML parsing         | Built into the JDK. No external dependency, works fine for the small XML files in this dataset.                |
| Java I/O (`BufferedReader`)                                | CSV parsing         | Hand-rolled quote-aware parser. Avoids pulling in OpenCSV for what is essentially a 30-line class.             |

### Storage

| Technology                       | Role                | Why it was chosen                                                                                |
| -------------------------------- | ------------------- | ------------------------------------------------------------------------------------------------ |
| [MySQL](https://www.mysql.com) 8.x | Relational database | Required by the brief. ACID-compliant, enforces referential integrity via foreign keys.          |
| [MySQL Workbench](https://www.mysql.com/products/workbench/) | GUI for schema management | Used to design and inspect the schema, run ad-hoc queries, and export the final `warehouse.sql`. |

### Application Layer

| Technology                          | Role              | Why it was chosen                                                                              |
| ----------------------------------- | ----------------- | ---------------------------------------------------------------------------------------------- |
| [PHP](https://www.php.net) 7.4+     | Middleware        | Required by the brief. A single-file `queryEngine.php` is enough for four endpoints.           |
| [mysqli](https://www.php.net/manual/en/book.mysqli.php) | DB driver | Built into PHP. Supports prepared statements for safe, parameterised queries.                  |
| HTML5 + vanilla JavaScript          | Front-end         | No framework needed. `fetch` + `innerHTML` is plenty for rendering a JSON response as a table. |
| CSS3                                | Styling           | Hand-written stylesheet with a small colour system for deal-stage pills.                       |
| [Apache](https://httpd.apache.org)  | Web server        | Required by the brief. Bundled with XAMPP alongside PHP.                                       |

### Tooling

| Tool                                                 | Purpose                          |
| ---------------------------------------------------- | -------------------------------- |
| [XAMPP](https://www.apachefriends.org)               | Local Apache + PHP + MySQL stack |
| [Git](https://git-scm.com) + [GitHub](https://github.com) | Version control and collaboration |
| [VS Code](https://code.visualstudio.com)             | Code editor for Java, PHP, HTML  |

---

## 🚀 Getting Started

### Prerequisites

- **JDK 25 or higher** — to compile and run the Java ETL ([Adoptium](https://adoptium.net) is a good free source)
- **Maven 3.6+** — to build the ETL project and run tests
- **MySQL 8.x** running on `localhost` (username `root`, password `rootroot` per the brief)
- **XAMPP** (or any Apache + PHP 7.4+ stack) for the web interface

### Step 1 — Clone the Repository

```bash
git clone https://github.com/Mahmud-Alam/crm-datawarehouse.git
cd crm-datawarehouse
```

### Step 2 — Create the Database

Open MySQL Workbench (or the `mysql` CLI) and run the schema file:

```bash
mysql -u root -p < data/database/warehouse.sql
# password: rootroot
```

This creates the `crm_warehouse` database, the four tables, the foreign key constraints, and a small set of sample rows so you can test the web interface immediately without running the ETL.

### Step 3 — Place the Source Data Files

Drop the four Maven Analytics dataset files into the matching locations (the repo ships with small sample files in the same format — overwrite them with the real files for the actual run):

| Real file (from professor) | Put it here                                |
| -------------------------- | ------------------------------------------ |
| `accounts.xml`             | `data/xml/accounts.xml`                    |
| `sales_teams.xml`          | `data/xml/sales_teams.xml`                 |
| `products.csv`             | `data/csv/products.csv`                    |
| `sales_pipeline.csv`       | `data/csv/sales_pipeline.csv`              |

### Step 4 — Build and Run the ETL

The ETL lives in the `etl/` directory, which is a standard Maven project.

```bash
cd etl

# Build the project (downloads mysql-connector-j and JUnit automatically)
mvn clean package

# Run the ETL pipeline
mvn exec:java -Dexec.mainClass="crm.ETL_Main"
```

You should see console output showing:

- Rows parsed from each CSV and XML file
- Sector standardisation applied (typo `technolgy` → `Technology`)
- `deal_stage` normalised to lowercase
- Rows loaded into each MySQL table
- Foreign-key resolution (`sales_agent` / `product` / `account` names → IDs)

### Step 5 — Deploy the Web Interface

Copy the `web/` folder into your Apache document root. With XAMPP:

- **Windows:** copy `web/` contents to `C:\xampp\htdocs\crm\`
- **macOS:** copy `web/` contents to `/Applications/XAMPP/htdocs/crm/`

Start Apache from the XAMPP control panel, then open in your browser:

```
http://localhost/crm/index.html
```

Click any of the four buttons to see the report or analysis.

### Useful Maven Commands

| Command              | Description                                                                |
| -------------------- | -------------------------------------------------------------------------- |
| `mvn clean package`  | Compile the project, run the unit tests, and package into `target/`        |
| `mvn test`           | Run the JUnit tests only — useful before committing                        |
| `mvn exec:java`      | Run the ETL pipeline                                                       |
| `mvn clean`          | Remove the `target/` directory for a clean rebuild                         |

---

## 📁 Project Structure

```
crm-datawarehouse/
├── data/
│   ├── csv/
│   │   ├── products.csv              # source: products (product, series, sales_price)
│   │   └── sales_pipeline.csv        # source: opportunities (opportunity_id, agent, product, ...)
│   ├── xml/
│   │   ├── accounts.xml              # source: accounts (account, sector, revenue, ...)
│   │   └── sales_teams.xml           # source: sales teams (sales_agent, manager, region)
│   └── database/
│       ├── warehouse.sql             # full schema + sample data
│       └── sampledata.sql            # smaller subset for quick testing
│
├── etl/                              # Maven project
│   ├── src/
│   │   ├── main/java/crm/
│   │   │   ├── XMLParser.java        # DOM-based parser, parametric row tag
│   │   │   ├── CSVParser.java        # quote-aware BufferedReader parser
│   │   │   ├── DataTransformer.java  # clean missing values + standardise sector + normalise deal_stage
│   │   │   ├── MySQLLoader.java      # JDBC PreparedStatement batch insert + name-to-ID lookups
│   │   │   └── ETL_Main.java         # orchestration: extract -> transform -> load
│   │   └── test/java/crm/
│   │       ├── TestCSV.java          # CSVParser unit tests
│   │       ├── TestXML.java          # XMLParser unit tests
│   │       ├── TestTransformer.java  # DataTransformer unit tests
│   │       └── TestMySQLLoader.java  # loader tests against in-memory DB
│   ├── target/                       # compiled output (gitignored)
│   └── pom.xml                       # Maven build file
│
├── web/                              # Apache document root (copy to htdocs/crm)
│   ├── index.html                    # four-button UI, fetch + render
│   ├── queryEngine.php               # PHP middleware, returns JSON
│   └── css/
│       └── style.css                 # styling, deal-stage pills
│
├── .gitignore
└── README.md                         # this file
```

---

## 🗄️ Database Schema

The `crm_warehouse` database has four tables with referential integrity enforced via foreign keys. Because the source data has no numeric IDs, every parent table uses `AUTO_INCREMENT` surrogate keys. The `opportunities` table is the only child table and has three foreign key constraints.

```sql
-- ACCOUNTS: companies that buy from us
CREATE TABLE accounts (
    account_id        INT AUTO_INCREMENT PRIMARY KEY,
    account_name      VARCHAR(150) NOT NULL,
    sector            VARCHAR(100),              -- standardised to Title Case by ETL
    year_established  INT,
    revenue           DECIMAL(12,2),             -- in millions of USD (per data dictionary)
    employees         INT,
    office_location   VARCHAR(100),
    subsidiary_of     VARCHAR(150)               -- maps to <subsidiary_of> in XML
);

-- PRODUCTS: catalogue of hardware we sell
CREATE TABLE products (
    product_id    INT AUTO_INCREMENT PRIMARY KEY,
    product_name  VARCHAR(150) NOT NULL,
    series        VARCHAR(100),
    sales_price   DECIMAL(10,2)
);

-- SALES_TEAMS: agents and their managers / regions
CREATE TABLE sales_teams (
    sales_agent_id    INT AUTO_INCREMENT PRIMARY KEY,
    sales_agent       VARCHAR(150) NOT NULL,
    manager           VARCHAR(150),
    regional_office   VARCHAR(100)
);

-- OPPORTUNITIES: the deal pipeline (child of all three tables above)
CREATE TABLE opportunities (
    opportunity_id    VARCHAR(20) PRIMARY KEY,   -- source uses string codes like "1C1I7A6R"
    sales_agent_id    INT,
    product_id        INT,
    account_id        INT,
    deal_stage        VARCHAR(20),               -- lowercase: prospecting / engaging / won / lost
    engage_date       DATE,
    close_date        DATE,
    close_value       DECIMAL(12,2),
    CONSTRAINT fk_opp_agent   FOREIGN KEY (sales_agent_id) REFERENCES sales_teams(sales_agent_id),
    CONSTRAINT fk_opp_product FOREIGN KEY (product_id)     REFERENCES products(product_id),
    CONSTRAINT fk_opp_account FOREIGN KEY (account_id)     REFERENCES accounts(account_id)
);
```

**Key design decisions:**

- `opportunity_id` is `VARCHAR(20)` because the source CSV uses 8-character alphanumeric codes (e.g. `1C1I7A6R`) — not integers.
- `revenue` is `DECIMAL(12,2)` in **millions of USD**, matching the data dictionary. A value of `1100.04` means 1.1 billion USD.
- `deal_stage` is `VARCHAR(20)` rather than `ENUM` so the ETL can normalise casing before loading, and so new stages can be added without altering the schema.
- Foreign keys are `ON DELETE RESTRICT` (the default), which means an opportunity cannot be deleted if it still references a parent row — this protects referential integrity.

---

## 🔌 Query Engine API

The PHP middleware exposes four endpoints. All accept a `report` query parameter via GET or POST and return a JSON envelope:

```json
{
  "label": "Products Report",
  "count": 7,
  "rows": [ { "product_id": "1", "product_name": "GTX Basic", ... } ]
}
```

| `report` value          | Description                                                                              |
| ----------------------- | ---------------------------------------------------------------------------------------- |
| `products`              | All products, ordered by `product_id`.                                                   |
| `opportunities`         | Won + lost opportunities joined to product, account, and sales agent.                    |
| `revenue_by_year`       | Total and average revenue grouped by `year_established`.                                 |
| `opportunity_analysis`  | Total and average `close_value` per product, ordered by total value descending.          |

### Example: Products Report Request/Response

```http
GET /crm/queryEngine.php?report=products HTTP/1.1
Host: localhost
```

```json
{
  "label": "Products Report",
  "count": 7,
  "rows": [
    { "product_id": "1", "product_name": "GTX Basic",      "series": "GTX", "sales_price": "550.00" },
    { "product_id": "2", "product_name": "GTX Pro",        "series": "GTX", "sales_price": "4821.00" },
    { "product_id": "3", "product_name": "MG Special",     "series": "MG",  "sales_price": "55.00" },
    { "product_id": "4", "product_name": "MG Advanced",    "series": "MG",  "sales_price": "3393.00" },
    { "product_id": "5", "product_name": "GTX Plus Pro",   "series": "GTX", "sales_price": "5482.00" },
    { "product_id": "6", "product_name": "GTX Plus Basic", "series": "GTX", "sales_price": "1096.00" },
    { "product_id": "7", "product_name": "GTK 500",        "series": "GTK", "sales_price": "26768.00" }
  ]
}
```

### Example: Opportunity Analysis Request

```http
GET /crm/queryEngine.php?report=opportunity_analysis HTTP/1.1
Host: localhost
```

```json
{
  "label": "Sales Opportunity Analysis by Product",
  "count": 7,
  "rows": [
    {
      "product_id": "7",
      "product_name": "GTK 500",
      "series": "GTK",
      "opportunity_count": "3",
      "total_value": "80304.00",
      "avg_value": "26768.00"
    },
    {
      "product_id": "2",
      "product_name": "GTX Pro",
      "series": "GTX",
      "opportunity_count": "5",
      "total_value": "24105.00",
      "avg_value": "4821.00"
    }
  ]
}
```

---

## 🎨 Design System

**Palette**

| Token              | Value                 | Usage                                                       |
| ------------------ | --------------------- | ----------------------------------------------------------- |
| Primary (navy)     | `#1e3a8a`             | Header background, button background, table header          |
| Active button      | `#0f766e`             | Active report button background                             |
| Hover              | `#1e40af`             | Button hover state                                          |
| Background         | `#f4f6f8`             | Page background                                             |
| Surface            | `#ffffff`             | Table background                                            |
| Stage: won         | `#dcfce7` / `#166534` | Pill background / text                                      |
| Stage: lost        | `#fee2e2` / `#991b1b` | Pill background / text                                      |
| Stage: engaging    | `#fef9c3` / `#854d0e` | Pill background / text                                      |
| Stage: prospecting | `#e0e7ff` / `#3730a3` | Pill background / text                                      |

**Principles**

- **Simple on purpose** — no CSS framework, no JavaScript framework. The front-end is one HTML file, one CSS file, and inline JavaScript. The point of the project is the integration work, not the UI.
- **Colour-coded deal stages** — every `deal_stage` value gets a consistent pill colour across the app, so won and lost deals are immediately distinguishable.
- **Money formatting** — every monetary column is formatted with thousands separators and two decimals via `toLocaleString('en-US')`, so `4514` renders as `4,514.00`.
- **Responsive by default** — flexbox-based button row and a fluid table layout work from 320px phone screens up to 1200px desktops without media queries.

---

## 🔐 Security & Data Integrity

- **Parameterised queries** — the PHP middleware uses `mysqli` with prepared statements. Even though no user input reaches the SQL (the only parameter is the `report` name, which is matched against a fixed `switch`), the discipline is maintained throughout.
- **SQL injection prevention on the Java side** — `MySQLLoader` uses `PreparedStatement` with `?` placeholders for every column value, including the strings parsed from CSV and XML.
- **Referential integrity** — three foreign key constraints on the `opportunities` table guarantee that no orphan rows can exist. If an opportunity references a product that has not been loaded, the ETL logs a warning and skips the row rather than producing a broken reference.
- **Idempotent loading** — every `loadTable` call runs `DELETE FROM <table>` before inserting, so the pipeline can be re-run as many times as needed without producing duplicate data.
- **NULL over empty string** — the transformer converts empty strings to `NULL` before loading, so SQL aggregate functions like `AVG` and `COUNT` behave correctly.
- **Local-only deployment** — the application is intended to run on `localhost` behind XAMPP. No production deployment, no authentication, no HTTPS. For a production system these would all need to be addressed.

---

## 🗺️ Roadmap

- [ ] **Chart visualisations** — render the analyses as bar/line charts using a lightweight library like Chart.js
- [ ] **Date-range filtering** — let the user filter opportunities by `engage_date` or `close_date`
- [ ] **Pagination** — for when the dataset grows beyond a few hundred rows
- [ ] **Authentication** — basic admin login so the reports are not publicly accessible
- [ ] **Export to CSV** — let the user download any report as a CSV file
- [ ] **More analyses** — win rate by sales agent, average deal cycle length, revenue by region
- [ ] **Scheduled ETL** — run the pipeline nightly via cron or Windows Task Scheduler
- [ ] **Docker setup** — bundle MySQL + Apache + PHP in a single `docker-compose.yml`
- [ ] **Integration tests** — end-to-end tests that load sample data and verify the report output

---

## 🚢 Deployment

The project is designed to run locally on a developer machine. There is no production deployment.

### Database

- **Host:** `localhost`
- **Port:** `3306`
- **Database:** `crm_warehouse`
- **Credentials:** `root` / `rootroot` (per the assessment brief — change for any real use)

### Web Server

- **Stack:** XAMPP (Apache 2.4 + PHP 7.4+)
- **Document root:** `htdocs/crm/`
- **URL:** `http://localhost/crm/index.html`

### ETL

- **Build:** `mvn clean package`
- **Run:** `mvn exec:java -Dexec.mainClass="crm.ETL_Main"`
- **Schedule (optional):** cron job or Windows Task Scheduler for nightly runs

---

## 🤝 Contributing

This is a coursework project, so contributions are not actively sought — but if you spot a bug or want to suggest an improvement, please feel free to:

1. Fork the repository and create a feature branch (`git checkout -b feature/amazing-feature`)
2. Commit your changes (`git commit -m "Add amazing feature"`)
3. Open a Pull Request

---

## 👨‍💻 Author

**Mahmud Alam**

- 🌐 [Portfolio Website](https://mahmudalam.com)
- 📧 Email: [mahmudalam.official@gmail.com](mailto:mahmudalam.official@gmail.com)
- 💻 [GitHub](https://github.com/Mahmud-Alam)
- 💼 [LinkedIn](https://www.linkedin.com/in/mahmudalamofficial)

---

## 🙏 Acknowledgements

- [Maven Analytics](https://mavenanalytics.io/data-playground) — for the CRM Sales Opportunities dataset that powers this project
- [MySQL](https://www.mysql.com) — for a rock-solid open-source relational database
- [Apache Friends](https://www.apachefriends.org) — for XAMPP, which made the local PHP + MySQL + Apache stack painless
- [Adoptium](https://adoptium.net) — for the free, high-quality JDK distribution
- The [Maven](https://maven.apache.org) team — for the build tool that manages dependencies so we never have to manually download a JAR again
- Everyone in the ICT914 cohort who tested early versions and gave honest feedback

---

## 📄 License

This project is licensed for educational use only. The dataset is © Maven Analytics and is used here for academic purposes in line with the unit's assessment brief.
