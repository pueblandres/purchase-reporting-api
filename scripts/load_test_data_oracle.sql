-- Generador de datos masivos para pruebas de reportes (Oracle)
-- Uso:
--   1) Conectarse con sqlplus/sqlcl al esquema de la app.
--   2) (Opcional) ajustar parámetros DEFINE.
--   3) Ejecutar: @scripts/load_test_data_oracle.sql
--
-- Ejemplo:
--   DEFINE NUM_USERS = 5000
--   DEFINE NUM_PRODUCTS = 1000
--   DEFINE NUM_PURCHASES = 50000
--   @scripts/load_test_data_oracle.sql

SET SERVEROUTPUT ON;

DEFINE RESET_DATA = Y
DEFINE NUM_USERS = 1000
DEFINE NUM_PRODUCTS = 500
DEFINE NUM_PURCHASES = 10000
DEFINE PURCHASE_DAYS_BACK = 180
DEFINE MAX_ITEMS_PER_PURCHASE = 6
DEFINE MAX_QTY_PER_ITEM = 8

DECLARE
    v_purchase_id           purchases.id%TYPE;
    v_product_id            products.id%TYPE;
    v_unit_price            products.price%TYPE;
    v_item_count            PLS_INTEGER;
    v_quantity              PLS_INTEGER;
    v_reset                 VARCHAR2(1) := UPPER(TRIM('&RESET_DATA'));
    v_num_users             PLS_INTEGER := &NUM_USERS;
    v_num_products          PLS_INTEGER := &NUM_PRODUCTS;
    v_num_purchases         PLS_INTEGER := &NUM_PURCHASES;
    v_days_back             PLS_INTEGER := &PURCHASE_DAYS_BACK;
    v_max_items_purchase    PLS_INTEGER := &MAX_ITEMS_PER_PURCHASE;
    v_max_qty_item          PLS_INTEGER := &MAX_QTY_PER_ITEM;
BEGIN
    IF v_reset = 'Y' THEN
        EXECUTE IMMEDIATE 'DELETE FROM purchase_items';
        EXECUTE IMMEDIATE 'DELETE FROM purchases';
        EXECUTE IMMEDIATE 'DELETE FROM products';
        EXECUTE IMMEDIATE 'DELETE FROM users';
        COMMIT;
    END IF;

    INSERT INTO users (name, email)
    SELECT 'User ' || lvl,
           'user' || lvl || '_' || TO_CHAR(SYSTIMESTAMP, 'YYYYMMDDHH24MISSFF3') || '@test.local'
    FROM (
        SELECT LEVEL lvl
        FROM dual
        CONNECT BY LEVEL <= v_num_users
    );

    INSERT INTO products (name, price)
    SELECT 'Product ' || lvl,
           ROUND(DBMS_RANDOM.VALUE(1, 2500), 2)
    FROM (
        SELECT LEVEL lvl
        FROM dual
        CONNECT BY LEVEL <= v_num_products
    );

    FOR i IN 1 .. v_num_purchases LOOP
        INSERT INTO purchases (user_id, total, purchase_date)
        VALUES (
            (
                SELECT id
                FROM (
                    SELECT id
                    FROM users
                    ORDER BY DBMS_RANDOM.VALUE
                )
                WHERE ROWNUM = 1
            ),
            0,
            SYSTIMESTAMP - NUMTODSINTERVAL(
                TRUNC(DBMS_RANDOM.VALUE(0, (v_days_back * 24 * 60) + 1)),
                'MINUTE'
            )
        )
        RETURNING id INTO v_purchase_id;

        v_item_count := TRUNC(DBMS_RANDOM.VALUE(1, v_max_items_purchase + 1));

        FOR j IN 1 .. v_item_count LOOP
            SELECT id, price
            INTO v_product_id, v_unit_price
            FROM (
                SELECT id, price
                FROM products
                ORDER BY DBMS_RANDOM.VALUE
            )
            WHERE ROWNUM = 1;

            v_quantity := TRUNC(DBMS_RANDOM.VALUE(1, v_max_qty_item + 1));

            INSERT INTO purchase_items (
                purchase_id,
                product_id,
                quantity,
                unit_price,
                subtotal
            ) VALUES (
                v_purchase_id,
                v_product_id,
                v_quantity,
                v_unit_price,
                ROUND(v_unit_price * v_quantity, 2)
            );
        END LOOP;
    END LOOP;

    UPDATE purchases p
    SET total = (
        SELECT NVL(SUM(pi.subtotal), 0)
        FROM purchase_items pi
        WHERE pi.purchase_id = p.id
    )
    WHERE p.total = 0;

    COMMIT;

    DBMS_OUTPUT.PUT_LINE('Carga finalizada.');
    DBMS_OUTPUT.PUT_LINE('Usuarios:   ' || TO_CHAR(v_num_users));
    DBMS_OUTPUT.PUT_LINE('Productos:  ' || TO_CHAR(v_num_products));
    DBMS_OUTPUT.PUT_LINE('Compras:    ' || TO_CHAR(v_num_purchases));
    DBMS_OUTPUT.PUT_LINE('Items máx.: ' || TO_CHAR(v_max_items_purchase));
END;
/
