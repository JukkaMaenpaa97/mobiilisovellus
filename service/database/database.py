from flask_mysqldb import MySQL
import MySQLdb

mysql = MySQL()

def query(query, params, single = False):
    cursor = mysql.connection.cursor()

    try:
        cursor.execute(query, params)
        mysql.connection.commit()

        if not cursor.rowcount:
            cursor.close()
            return None

    except (MySQLdb.Error, MySQLdb.Warning) as e:
        print(e)
        cursor.close()
        return None

    try:
        if single:
            result = cursor.fetchone()
        else:
            result = cursor.fetchall()

        cursor.close()
        return result
    except TypeError as e:
        print(e)
        cursor.close()
        return None
