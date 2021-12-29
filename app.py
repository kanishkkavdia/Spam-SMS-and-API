from flask import Flask, request, jsonify
import numpy as np
import json
import tensorflow as tf
from tensorflow import keras
import tensorflow_text as text

app = Flask(__name__)


@app.route("/api/", methods=["POST"])
def makecalc():
    data = request.get_json()
    print(data)
    model =tf.keras.models.load_model("sms_flagger")
    # prediction=model.predict(data)
    raw_output=model.predict(data)
    prediction=[]
    for x in raw_output:
        if int(x)>=30:
            prediction.append("Potential Spam")
        else:
            prediction.append("Look's clean")
    # prediction=np.array2string(model.predict(data))

    return jsonify(prediction)

if __name__ == "__main__":
    # new_model =tf.keras.models.load_model("sms_flagger")
    app.run(host="0.0.0.0", debug=True)


# reviews = [
#     'Reply to win Â£100 weekly! Where will the 2006 FIFA World Cup be held? Send STOP to 87239 to end service',
#     'You are awarded a SiPix Digital Camera! call 09061221061 from landline. Delivery within 28days. T Cs Box177. M221BP. 2yr warranty. 150ppm. 16 . p pÂ£3.99',
#     'it to 80488. Your 500 free text messages are valid until 31 December 2005.',
#     'Hey Sam, Are you coming for a cricket game tomorrow',
#     "Why don't you wait 'til at least wednesday to see if you get your .",
#     "Dear customer, your xxx bank account will be suspended! Please Re KYC Verification Update click here link http://446bdf227fc4.ngrok.io/xxxbank"
# ]
# print(new_model.predict(reviews))