from flask_mysqldb import MySQL

mysql = MySQL()

def query(query, params, single = False):
    cursor = mysql.connection.cursor()
    cursor.execute(query, params)
    mysql.connection.commit()
    if single:
        result = cursor.fetchone()
    else:
        result = cursor.fetchall()
    return result
