#!/usr/bin/env python
# encoding: utf-8
import json
from flask import Flask, jsonify
from flask import request
from service import MetaTraderService
from utils import NpEncoder
import py_eureka_client.eureka_client as eureka_client

rest_port = 5080
eureka_client.init(eureka_server="http://localhost:8761/eureka",
                   app_name="mt5-service",
                   instance_port=rest_port)


app = Flask(__name__)
service = MetaTraderService()

def __map_to_response(result):
    response = app.response_class(
        response = json.dumps(result, cls=NpEncoder),
        mimetype = 'application/json'
    )
    return response


@app.route('/')
def index():
    return json.dumps({'app': 'python mt5 connector'})


@app.route('/api/v1/mt5/symbols')    
def symbols():
    return service.get_symbols()

@app.route('/api/v1/mt5/symbol_info/<string:symbol>')
def symbol_info(symbol: str):
    return service.symbol_info(symbol)


@app.route('/api/v1/mt5/symbol_info_tick/<string:symbol>')
def symbol_info_tick(symbol: str):
    return service.symbol_info_tick(symbol)


#number of seconds elapsed since 1970.01.01
#zwraca dane z przed podanej daty
@app.route('/api/v1/mt5/copy_rates_from/<string:symbol>/<string:timeframe>/<int:date_from>/<int:count>')
def copy_rates_from(symbol: str, timeframe: str, date_from: int, count: int):
    result = service.copy_rates_from(symbol, timeframe, date_from, count)
    return __map_to_response(result)


@app.route('/api/v1/mt5/copy_rates_from_pos/<string:symbol>/<string:timeframe>/<int:count>', methods=['GET'])
def copy_rates_from_pos(symbol: str, timeframe: str, count: int):  
    print(f"args: symbol-{symbol} timeframe-{timeframe} count-{count}")
    result = service.copy_rates_from_pos(symbol, timeframe, count)
    return __map_to_response(result)


@app.route('/api/v1/mt5/copy_rates_range/<string:symbol>/<string:timeframe>/<int:utc_from>/<int:utc_to>', methods=['GET'])
def copy_rates_range(symbol: str, timeframe: str, utc_from: int, utc_to: int):      
    result = service.copy_rates_range(symbol, timeframe, utc_from, utc_to)
    return __map_to_response(result)


@app.route('/api/v1/mt5/order_calc_margin/<string:symbol>/<string:action>/<float:lot>')
def order_calc_margin(symbol: str, action: str, lot: float):
    result = service.order_calc_margin(symbol, action, lot)
    return __map_to_response(result)


@app.route('/api/v1/mt5/order_calc_profit/<string:symbol>/<string:action>/<float:lot>/<float:price_open>/<float:price_close>')
def order_calc_profit(symbol: str, action: str, lot: float, price_open: float, price_close: float):
    result = service.order_calc_profit(symbol, action, lot, price_open, price_close)
    return __map_to_response(result)


@app.route('/api/v1/mt5/orders_get')    
def orders_get():
    result = service.orders_get()
    return __map_to_response(result)  


@app.route('/api/v1/mt5/orders_get/symbol/<string:value>')    
def orders_get_symbol(value: str):
    result = service.orders_get(symbol=value)
    return __map_to_response(result)   


@app.route('/api/v1/mt5/orders_get/ticket/<int:value>')    
def orders_get_ticket(value: int):
    result = service.orders_get(ticket=value)
    return __map_to_response(result)       


@app.route('/api/v1/mt5/orders_total')    
def orders_total():
    result = service.orders_total()
    return __map_to_response(result)    


@app.route('/api/v1/mt5/positions_get')    
def positions_get():
    result = service.positions_get()
    return __map_to_response(result)  


@app.route('/api/v1/mt5/positions_get/symbol/<string:value>')    
def positions_get_symbol(value: str):
    result = service.positions_get(symbol=value)
    return __map_to_response(result)   


@app.route('/api/v1/mt5/positions_get/ticket/<int:value>')    
def positions_get_ticket(value: int):
    result = service.positions_get(ticket=value)
    return __map_to_response(result)   
 

@app.route('/api/v1/mt5/positions_total')    
def positions_total():
    result = service.positions_total()
    return __map_to_response(result)    


@app.route('/api/v1/mt5/history_orders/<int:from_date>/<int:to_date>')    
def history_orders(from_date: int, to_date: int):
    result = service.history_orders(from_date, to_date)
    return __map_to_response(result)    


@app.route('/api/v1/mt5/history_orders/<int:from_date>/<int:to_date>/<string:group>')    
def history_orders_group(from_date: int, to_date: int, group: str):
    result = service.history_orders(from_date, to_date, group)
    return __map_to_response(result)    


@app.route('/api/v1/mt5/account_info')    
def account_info():
    result = service.account_info()
    return __map_to_response(result)        



app.run(host="0.0.0.0", port=rest_port, debug=True)
