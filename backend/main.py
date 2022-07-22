import json
from flask import Flask, request
from flask import jsonify

app = Flask(__name__)


class Item:
    def __init__(self, name, price, id):
        self.name = name
        self.price = price
        self.id = id

    def serialize(self):
        return {
            'name': self.name,
            'price': self.price,
            'id': self.id,
        }


items = [Item('salt', 10, 0), Item('pepper', 5, 1), Item('carry', 5, 2)]


@app.route("/")
def hello_world():

    return jsonify(data=[e.serialize() for e in items])


@app.route('/<int:id>/')
def item(id):
    for e in items:
        if e.id == id:
            return jsonify(e.serialize())
    return jsonify(
        error='error')


@app.route('/<int:id>/edit/', methods=['POST'])
def edit(id):
    request_data = request.get_json()
    for index, e in enumerate(items):
        if e.id == id:
            if 'name' in request_data:
                if isinstance(request_data['name'], str):
                    items[index].name = request_data['name']
                else:
                    return jsonify(error="Has to be a string")
            if 'price' in request_data:
                if isinstance(request_data['price'], int) or isinstance(request_data['price'], float):
                    items[index].price = request_data['price']
                else:
                    return jsonify(
                        error='Price has to be numerical'
                    )
            return jsonify(items[index].serialize())
    return jsonify(
        error='No item with that id')


@app.route('/<int:id>/', methods=['DELETE'])
def delete(id):
    for e in items:
        if e.id == id:
            items.remove(e)
            return jsonify(delete=True)
    return jsonify(delete=False)


@app.route('/add/', methods=['POST'])
def add():
    request_data = request.get_json()
    if 'name' in request_data and 'price' in request_data:
        item = Item(
            name=request_data['name'],
            price=request_data['price'],
            id=items[-1].id + 1
        )
        items.append(item)
        return jsonify(item.serialize())
    elif 'name' in request_data:
        return jsonify(error='No Price selected')
    elif 'price' in request_data:
        return jsonify(error='No Price selected')
