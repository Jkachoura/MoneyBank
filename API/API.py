import dbm
import requests
import json

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
            

            return(make_response(jsonify("Wrong body!"), 400))

        iban = data["IBAN"]
        

        pincode = data["Pincode"]
        # check blockstatus
        mycursor.execute(f"SELECT BlockStatus FROM accounts WHERE IBAN = '{iban}'")
        results = mycursor.fetchone()
        
        if (int(results[0]) == 1):
            mycursor.execute(f"UPDATE accounts SET Attempts = 0 WHERE IBAN = '{iban}' ")
            moneybank.commit()
            return (make_response(jsonify("Card Blocked!")), 403)

        # check attempts
        mycursor.execute(f"SELECT Attempts FROM accounts WHERE IBAN = '{iban}'")
        results = mycursor.fetchone()

        print(results[0])
        if (int(results[0]) < 3):
            # if (iban == f"{iban}"):
            mycursor.execute(f"SELECT pinCode FROM accounts WHERE IBAN = '{iban}'")
            results = mycursor.fetchone()

            if(int(results[0]) == pincode):
                mycursor.execute(f"UPDATE accounts SET Attempts = 0 WHERE IBAN = '{iban}' ")
                moneybank.commit()
                mycursor.execute(f"SELECT Balance FROM accounts WHERE IBAN = '{iban}'")
                results= mycursor.fetchone()
                print(results)
                return make_response(jsonify(results[0]), 200)
            else:
                mycursor.execute(f"SELECT Attempts FROM accounts WHERE IBAN = '{iban}'")
                results = mycursor.fetchone()
                newAttempt = int(results[0]) + 1

                mycursor.execute(f"UPDATE accounts SET Attempts = '{newAttempt}' WHERE IBAN = '{iban}' ")

                moneybank.commit()

                mycursor.execute(f"SELECT Attempts FROM accounts WHERE IBAN = '{iban}'")
                results = mycursor.fetchone()
                print(results)
                return make_response(jsonify( int(3 - newAttempt)), 401)
        else:
            mycursor.execute(f"UPDATE accounts SET Attempts = 0 WHERE IBAN = '{iban}' ")
            moneybank.commit()
            mycursor.execute(f"UPDATE accounts SET BlockStatus = 1 WHERE IBAN = '{iban}'")
            moneybank.commit()
            return (make_response(jsonify("Card Blocked!")),403)

    except:
    
        
        return(make_response(jsonify("Account does not exist")),404)

@app.route('/withdraw', methods=['GET', 'POST'])
def withdraw():
    try:
        data = request.json
        if not "IBAN" in data or not "Pincode" in data or not "Amount":
            return make_response(jsonify("Wrong body!"), 400)

        iban = data["IBAN"]
        pincode = data["Pincode"]
        amount = data["Amount"]

        # check block status
        mycursor.execute(f"SELECT BlockStatus FROM accounts WHERE IBAN = '{iban}'")
        results = mycursor.fetchone()

        if (int(results[0]) == 1):
            mycursor.execute(f"UPDATE accounts SET Attempts = 0 WHERE IBAN = '{iban}' ")
            moneybank.commit()
            return (make_response(jsonify("Card Blocked!")), 403)

        # check attempts
        mycursor.execute(f"SELECT Attempts FROM accounts WHERE IBAN = '{iban}'")
        results = mycursor.fetchone()

        print(results[0])
        if (int(results[0]) < 3):
            mycursor.execute(f"SELECT pinCode FROM accounts WHERE IBAN = '{iban}'")
            results = mycursor.fetchone()

            if (int(results[0]) == pincode):
                # correcte pincode
                mycursor.execute(f"UPDATE accounts SET Attempts = 0 WHERE IBAN = '{iban}' ")
                moneybank.commit()
                mycursor.execute(f"SELECT Balance FROM accounts WHERE IBAN = '{iban}'")
                results = mycursor.fetchone()

                if (int(results[0]) < amount):
                    return make_response(jsonify("insufficient balance!"), 406)
                
                newbalance = results[0] - amount
                mycursor.execute(f"UPDATE accounts SET Balance = '{newbalance}'  WHERE IBAN = '{iban}' ")
                moneybank.commit()
                # bedrag van balans afhalen
                return make_response(jsonify(newbalance), 200)
            else:
                # foutieve pincode
                mycursor.execute(f"SELECT Attempts FROM accounts WHERE IBAN = '{iban}'")
                results = mycursor.fetchone()
                newAttempt = int(results[0]) + 1

                mycursor.execute(f"UPDATE accounts SET Attempts = '{newAttempt}' WHERE IBAN = '{iban}' ")
                moneybank.commit()

                mycursor.execute(f"SELECT Attempts FROM accounts WHERE IBAN = '{iban}'")
                results = mycursor.fetchone()
                print(results)
                return make_response(jsonify(3 - newAttempt), 401)
        else:
            # account blokkeren
            mycursor.execute(f"UPDATE accounts SET Attempts = 0 WHERE IBAN = '{iban}' ")
            moneybank.commit()
            mycursor.execute(f"UPDATE accounts SET BlockStatus = 1 WHERE IBAN = '{iban}'")
            moneybank.commit()
            return (make_response(jsonify("Card Blocked!")), 403)

    except:
        
        return (make_response(jsonify("Account does not exist")), 404)



if __name__ == '__main__':
    app.run(host = '0.0.0.0', port = 8000)
