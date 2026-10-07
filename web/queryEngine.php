<?php

$host = "localhost";
$user = "root";
$password = "root12";
$database = "crm_warehouse";

$conn = new mysqli($host, $user, $password, $database);

if ($conn->connect_error) {
    die("Database connection failed: " . $conn->connect_error);
}

$conn->set_charset("utf8");

$query = $_GET["query"] ?? "summary";
$product = $_GET["product"] ?? "";
$stage = $_GET["stage"] ?? "";

if ($query === "summary") {

    $sql = "
        SELECT
            COUNT(*) AS total_opportunities,
            SUM(CASE WHEN deal_stage = 'Won' THEN 1 ELSE 0 END) AS won_opportunities,
            SUM(CASE WHEN deal_stage = 'Lost' THEN 1 ELSE 0 END) AS lost_opportunities,
            SUM(close_value) AS total_sales
        FROM sales_pipeline
    ";

} elseif ($query === "products") {

    $sql = "
        SELECT
            p.product,
            p.series,
            COUNT(sp.opportunity_id) AS opportunities,
            SUM(sp.close_value) AS total_sales
        FROM products p
        LEFT JOIN sales_pipeline sp
            ON p.product_id = sp.product_id
        GROUP BY p.product_id, p.product, p.series
        ORDER BY total_sales DESC
    ";

} elseif ($query === "agents") {

    $sql = "
        SELECT
            st.sales_agent,
            st.manager,
            st.regional_office,
            COUNT(sp.opportunity_id) AS opportunities,
            SUM(sp.close_value) AS total_sales
        FROM sales_teams st
        LEFT JOIN sales_pipeline sp
            ON st.sales_agent_id = sp.sales_agent_id
        GROUP BY
            st.sales_agent_id,
            st.sales_agent,
            st.manager,
            st.regional_office
        ORDER BY total_sales DESC
    ";

} elseif ($query === "accounts") {

    $sql = "
        SELECT
            a.account,
            a.sector,
            a.revenue,
            COUNT(sp.opportunity_id) AS opportunities,
            SUM(sp.close_value) AS total_sales
        FROM accounts a
        LEFT JOIN sales_pipeline sp
            ON a.account_id = sp.account_id
        GROUP BY
            a.account_id,
            a.account,
            a.sector,
            a.revenue
        ORDER BY total_sales DESC
    ";

} else {

    die("Invalid query.");

}

$result = $conn->query($sql);

if (!$result) {
    die("Query failed: " . $conn->error);
}

$data = [];

while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

header("Content-Type: application/json");

echo json_encode($data);

$conn->close();

?>