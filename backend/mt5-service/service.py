import json
import MetaTrader5 as mt5
import pytz
from datetime import datetime


class MetaTraderService:
    def __init__(self):
        self.login = 1021359
        self.password = 'XX3ZetLq'  # uktEfqd4
        self.server = 'TradeQuo-Server'
        self.timezone = pytz.timezone("Etc/UTC")

        if not mt5.initialize(login=self.login, server=self.server, password=self.password):
            raise Exception(
                "initialize() failed, error code = ", mt5.last_error())

    def shutdown(self):
        mt5.shutdown()

    def __map_timeframe(self, value: str):
        if 'M1' == value:
            return mt5.TIMEFRAME_M1
        if 'M5' == value:
            return mt5.TIMEFRAME_M5
        if 'M15' == value:
            return mt5.TIMEFRAME_M15
        if 'M30' == value:
            return mt5.TIMEFRAME_M30
        if 'H1' == value:
            return mt5.TIMEFRAME_H1
        if 'H4' == value:
            return mt5.TIMEFRAME_H4
        if 'D1' == value:
            return mt5.TIMEFRAME_D1
        if 'W1' == value:
            return mt5.TIMEFRAME_W1
        if 'MN' == value:
            return mt5.TIMEFRAME_MN1
        raise Exception(f"Invalide timeframe - {value}")

    def __map_rates_to_dict(self, rates):
        result = []
        for record in rates:
            result.append(
                {
                     "time" : record[0],
                     "open" : record[1],
                     "high" : record[2],
                     "low" : record[3],
                     "close" : record[4],
                     "tickVolume" : record[5],
                     "spread" : record[6],
                     "realVolume" : record[7]
                }
            )
        return result


    def __map_order_type(self, value: str):
        if 'BUY' == value:
            return mt5.ORDER_TYPE_BUY
        if 'SELL' == value:
            return mt5.ORDER_TYPE_SELL
        if 'BUY_STOP' == value:
            return mt5.ORDER_TYPE_BUY_STOP
        if 'SELL_STOP' == value:
            return mt5.ORDER_TYPE_SELL_STOP
        raise Exception(f"Invalide timeframe - {value}")

    def get_symbols(self):
        symbols = mt5.symbols_get()
        result = []
        for s in symbols:
            result.append(s.name)
        return result

    def symbol_info(self, symbol: str):
        data = mt5.symbol_info(symbol)
        if data != None:
            symbol_info_dict = data._asdict()
            return symbol_info_dict
        else:
            raise Exception(
                f"Fail to get symbol_info('{symbol}'), error code = ", mt5.last_error())

    def symbol_info_tick(self, symbol: str):
        selected = mt5.symbol_select(symbol, True)
        if not selected:
            raise Exception(
                f"Failed to select {symbol}, error code =", mt5.last_error())
        lasttick = mt5.symbol_info_tick(symbol)._asdict()
        return lasttick

    def copy_rates_from(self, symbol: str, timeframe: str, date_from, count: int):
        _timeframe = self.__map_timeframe(timeframe)
        rates = mt5.copy_rates_from(symbol, _timeframe, date_from, count)
        return self.__map_rates_to_dict(rates)

    def copy_rates_from_pos(self, symbol, timeframe, count=20):
        _timeframe = self.__map_timeframe(timeframe)
        rates = mt5.copy_rates_from_pos(symbol, _timeframe, 0, count)
        return self.__map_rates_to_dict(rates)


    def copy_rates_range(self, symbol, timeframe, utc_from, utc_to):
        _timeframe = self.__map_timeframe(timeframe)
        rates = mt5.copy_rates_range(symbol, _timeframe, utc_from, utc_to)
        return self.__map_rates_to_dict(rates)
          

    def order_calc_margin(self, symbol, action, lot):
        order_type = self.__map_order_type(action)
        ask = mt5.symbol_info_tick(symbol).ask
        margin = mt5.order_calc_margin(order_type, symbol, lot, ask)
        return margin


    def order_calc_profit(self, symbol, action, lot, price_open, price_close):
        order_type = self.__map_order_type(action)
        profit = mt5.order_calc_profit(order_type, symbol, lot, price_open, price_close)
        return profit


    def orders_get(self, symbol=None, ticket=None):
        orders = mt5.orders_get()
        if symbol is not None: 
            orders = mt5.orders_get(symbol=symbol)
        if ticket is not None:
            orders = mt5.orders_get(ticket=ticket)
        if orders is None or len(orders) == 0:
            return []

        result = []
        for record in orders:
            result.append(dict(record._asdict()))

        return  result


    def orders_total(self):
        return mt5.orders_total()


    def positions_get(self, symbol=None, ticket=None):
        positions = mt5.positions_get()
        if symbol is not None: 
            positions = mt5.positions_get(symbol=symbol)
        if ticket is not None:
            positions = mt5.positions_get(ticket=ticket)
        if positions is None or len(positions) == 0:
            return []

        result = []
        for record in positions:
            result.append(dict(record._asdict()))
        return result


    def positions_total(self):
        return mt5.positions_total()


    def history_orders(self, from_date, to_date, group="*"):
        history_orders = mt5.history_deals_get(from_date, to_date, group=group)        
        result = []
        for record in history_orders:
            result.append(dict(record._asdict()))
        return result


    def account_info(self):
        return mt5.account_info()._asdict()