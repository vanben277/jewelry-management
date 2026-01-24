import json
import random
import re
import requests

# Load dữ liệu hội thoại từ JSON
try:
    with open('extrafiles/NormalChat.json', encoding='utf-8') as file:
        normal_chat_data = json.load(file)
except FileNotFoundError:
    normal_chat_data = {}

API_URL = "http://localhost:8080/api/v1/product/search"

def save_to_normal_chat(query, response, language="vi"):
    """Lưu câu hỏi và câu trả lời vào NormalChat.json"""
    try:
        query_key = query.lower().strip()
        if query_key in normal_chat_data:
            if language in normal_chat_data[query_key]:
                normal_chat_data[query_key][language].append(response)
            else:
                normal_chat_data[query_key][language] = [response]
        else:
            normal_chat_data[query_key] = {language: [response]}
        
        with open('extrafiles/NormalChat.json', 'w', encoding='utf-8') as file:
            json.dump(normal_chat_data, file, ensure_ascii=False, indent=4)
        print(f"Saved to NormalChat.json: {query_key} -> {response}")
    except Exception as e:
        print(f"Error saving to NormalChat.json: {e}")

def reply(query: str):
    query = query.strip()
    language = "vi" if any(char in query for char in "àáãạảăắằẳẵặâấầẩẫậèéẹẻẽêềếểễệìíĩỉịòóõọỏôốồổỗộơớờởỡợùúũụủưứừửữựỳýỹỷỵ") else "en"

    query_cleaned = re.split(r'\|', query)[0].strip()
    print(f"Query after cleaning: {query_cleaned}")

    # Gọi API Spring Boot để tìm sản phẩm
    try:
        response = requests.get(API_URL, params={"name": query_cleaned}, timeout=5)
        if response.status_code == 200:
            result = response.json()
            print(f"Product found: {result}")

            #  Kiểm tra có sản phẩm không
            if (isinstance(result, dict) and "data" in result and 
                result["data"] and "content" in result["data"] and 
                len(result["data"]["content"]) > 0):
                
                products_data = result["data"]["content"]
                total_found = result["data"].get("totalElements", len(products_data))
                
                #  Format lại data cho từng sản phẩm
                formatted_products = []
                for product in products_data:
                    formatted_product = {
                        "id": product.get("id"),
                        "name": product.get("name"),
                        "displayName": product.get("displayName"),
                        "price": float(product.get("price", 0)),
                        "quantity": product.get("quantity", 0),
                        "image": product.get("primaryImageUrl"),
                        "images": product.get("images", []),
                        "sku": product.get("sku"),
                        "category": product.get("categoryName"),
                        "status": product.get("status"),
                        "goldType": product.get("goldType"),
                        "sizes": product.get("sizes", []),
                        "soldQuantity": product.get("soldQuantity", 0)
                    }
                    formatted_products.append(formatted_product)
                
                print(f"Formatted products: {formatted_products}")  # Debug log
                
                return {
                    "type": "products",
                    "data": {
                        "products": formatted_products,
                        "total": total_found,
                        "query": query_cleaned
                    }
                }
    except requests.RequestException as e:
        print(f"Error calling product API: {e}")

    # Fallback sang NormalChat.json
    query_lower = query.lower()
    for key in normal_chat_data.keys():
        questions = [q.strip().lower() for q in key.split("/")]
        if query_lower in questions or any(q in query_lower for q in questions):
            responses = normal_chat_data[key].get(language, [])
            if responses:
                selected_response = random.choice(responses) if isinstance(responses, list) else responses
                return {"type": "text", "reply": selected_response}

    # Default response
    default_response = "Xin lỗi, tôi không hiểu. Bạn có thể hỏi nội dung khác được không?"
    save_to_normal_chat(query, default_response, language)
    return {"type": "text", "reply": default_response}