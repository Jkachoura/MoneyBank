import dbm
from flask import Flask, jsonify, request, make_response
import mysql.connector

app = Flask(__name__)

moneybank = mysql.connector.connect(
    host="localhost",
    user="root",
    password="MNBK22",
    database="moneybank"
    )
mycursor = moneybank.cursor()




@app.route('/balance', methods = ['GET', 'POST'])
def check_balance():
    try:
        data= request.json
        if not "IBAN" in data or not "Pincode" in data:
            return(make_response(jsonify("Foute body!"), 400))
        iban = data["IBAN"]
        pincode = data["Pincode"]
        #balance = data["Balance"]

        mycursor.execute(f"SELECT pinCode FROM accounts WHERE IBAN = '{iban}'")
        results = mycursor.fetchone()
        print(results[0])
        if(int(results[0]) == pincode):
        # if (iban == f"{iban}"):
            mycursor.execute(f"SELECT Balance FROM accounts WHERE IBAN = '{iban}'")
            results = mycursor.fetchall()
            return(make_response(jsonify(results[0]), 200))
        else: return(make_response(jsonify("Foute pincode")),404)

    
    except:
        return(make_response(jsonify("Account does not exist")),404)

@app.route('/withdraw', methods = ['GET', 'POST'])
def withdraw():
    try:
        
        data = request.json
        if not "IBAN" in data or not "Pincode" in data or not "Amount":
             return(make_response(jsonify("Foute body!"), 400))
        iban = data["IBAN"]
        pincode = data["Pincode"]
        amount = data["Amount"]
        mycursor.execute(f"SELECT pinCode FROM accounts WHERE IBAN = '{iban}'")
        results = mycursor.fetchone()
        print(results[0])
        if(int(results[0]) == pincode):
            if(amount > 300):
                return(make_response(jsonify("Amount exceeded!")),400 )
            mycursor.execute(f"SELECT Balance FROM accounts WHERE IBAN = '{iban}'")
            results = mycursor.fetchone()
            newbalance = results[0] - amount
            mycursor.execute(f"UPDATE accounts SET Balance = '{newbalance}'  WHERE IBAN = '{iban}' ")
            moneybank.commit()
        else: return(make_response(jsonify("Foute pincode")),404)
        return(make_response(jsonify(results[0]), 200))

    except:
        
        return(make_response(jsonify("Account does not exist")),404)

if __name__ == '__main__':
    app.run(host = '0.0.0.0')
