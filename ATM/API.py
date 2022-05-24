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

mycursor.execute("SELECT * FROM customers WHERE FirstName= 'Sam'")

myresult = mycursor.fetchall()

for x in myresult:
  print(x)

@app.route('/balance', methods = ['GET'])
def check_balance():
    data= request.json
    iban = data["IBAN"]
    pincode = data["Pincode"]
    #balance = data["Balance"]
    print(iban)
    print(pincode)
   # print(balance)
    if (iban == "IBAN123"):
        return(make_response(jsonify("OK!"), 200))
    return(make_response(jsonify("Account does not exist")),404)

@app.route('/withdraw', methods = ['GET'])
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