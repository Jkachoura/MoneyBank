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
    data= request.json
    if not "IBAN" in data or not "Pincode" in data:
        return(make_response(jsonify("Foute body!"), 400))
    iban = data["IBAN"]
    pincode = data["Pincode"]
    #balance = data["Balance"]

    mycursor.execute(f"SELECT pinCode FROM accounts WHERE IBAN = '{iban}'")
    results = mycursor.fetchall()
    print(results[0])
    if (iban == f"{iban}"):
        return(make_response(jsonify("OK!"), 200))
    return(make_response(jsonify("Account does not exist")),404)
    

@app.route('/withdraw', methods = ['GET', 'POST'])
def withdraw():
    data = request.json
    iban = data["IBAN"]
    pincode = data["Pincode"]
    amount = data["Amount"]
    print(iban)
    print(pincode)
    print(amount)
    if (amount > 300):
        return(make_response(jsonify("Amount exceeded!"), 400)) 
    return(make_response(jsonify("OK!"), 200))

if __name__ == '__main__':
    app.run(host = '0.0.0.0')
