from flask_mysqldb import MySQL

mysql = MySQL()

def query(query, params, single = False):
    cursor = mysql.connection.cursor()

    try:
        cursor.execute(query, params)
        mysql.connection.commit()
        if not cursor.rowcount:
            return None
            
    except (MySQLdb.Error, MySQLdb.Warning) as e:
        print(e)
        return None

    try:
        if single:
            result = cursor.fetchone()
        else:
            result = cursor.fetchall()
        return result
    except TypeError as e:
        print(e)
        return None
