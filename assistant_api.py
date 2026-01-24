from flask import Flask, request, jsonify
from flask_cors import CORS
import sys
import signal

def signal_handler(sig, frame):
    print('You pressed Ctrl+C! Exiting gracefully...')
    sys.exit(0)

signal.signal(signal.SIGINT, signal_handler)


sys.path.append('D:\\Learn\\Do_An_Ki\\Assistant')
from normalChat import reply as normal_chat_reply

app = Flask(__name__)
CORS(app, resources={r"/api/*": {"origins": "*"}})

@app.route('/api/chat', methods=['POST'])
def chat_endpoint():
    data = request.get_json()
    message = data.get('message', '').strip()
    if not message:
        return jsonify({'error': 'Please send a message'}), 400
    
    # Gọi normalChat.reply
    response = normal_chat_reply(message)
    
    # Kiểm tra nếu response là dict (thông tin sản phẩm) hoặc chuỗi (câu trả lời thông thường)
    if isinstance(response, dict) and 'type' in response:
        return jsonify(response)  # Trả về trực tiếp {"type": "product", "data": {...}}
    else:
        return jsonify({'reply': response})  # Trả về {"reply": "..."}

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)