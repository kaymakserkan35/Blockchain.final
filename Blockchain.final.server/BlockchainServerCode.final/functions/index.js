const timeZone = "UTC+3 (TRT)";
const { initializeApp, applicationDefault, cert } = require('firebase-admin/app');
const https = require('https');
const http = require('http');
const functions = require('firebase-functions');
const info = functions.config().info;
const administor = require('firebase-admin');
const { service } = require('firebase-functions/v1/analytics');
const { log } = require('firebase-functions/logger');
const { Server } = require('http');
const { resolve } = require('path');
const { setPriority } = require('os');
const { error } = require('console');
const { url } = require('inspector');


administor.initializeApp(functions.config());
const baseCurrency = "USD";
/*---------------------------------------------------------------------------------------------------------------------------------------------------------*/
class ErrorResponse {
  constructor(header = "", message = "") {
    this.header = header;
    this.message = message;
    H.errorLog(ErrorResponse.name, "", message);
  }
  
  message = "";
}
class H {
  static errorLog(className = "", methodName = "", errorMessage = "") {
    console.error(className + ":" + methodName + "-->" + errorMessage);
  }
  static infoLog(className = "", methodName = "", errorMessage = "") {
    console.info(className + ":" + methodName + "-->" + errorMessage);
  }
  static warnLog(className = "", methodName = "", errorMessage = "") {
    console.warn(className + ":" + methodName + "-->" + errorMessage);
  }

}
class MyDate {
  dateObj;
  timestamp = 0;  // specific time in milliseconds..
  day = 0;    //  1 of 7
  date = 0;   //  1 of 31
  year = 0;   //  2022
  month = 0;  //  1 of 12 --> 0 of 11
  hour = "";  //  12 : 50
  constructor(timeZone = "Londra/Greenwich") {
    this.dateObj = new Date();
    this.hour = this.dateObj.getUTCHours() + ":" + this.dateObj.getUTCMinutes();
    this.timestamp = this.dateObj.getTime();
    this.day = this.dateObj.getUTCDay();
    this.date = this.dateObj.getUTCDate();
    this.year = this.dateObj.getUTCFullYear();
    this.month = this.dateObj.getUTCMonth() + 1;
    //H.infoLog(MyDate.name, "constructor", this.getCalendar() + " ---  " + this.hour);
  }
  getCalendar() {
    var date = this.date.toString();
    var month = this.month.toString();
    var year = this.year.toString();
    if (parseInt(date) < 10) {
      date = "0" + date;
    }
    if (parseInt(month) < 10) {
      month = "0" + month;
    }
    return (year + "-" + month + "-" + date);
  }

  isTimeBetween(hourSmall = "", hourLarge = "") {
    var hourSmallMinutes = parseInt(hourSmall.split(":")[0]) * 60 + parseInt(hourSmall.split(":")[1]);
    var hourLargeMinutes = parseInt(hourLarge.split(":")[0]) * 60 + parseInt(hourLarge.split(":")[1]);
    var hourNowMinutes = parseInt(this.hour.split(":")[0]) * 60 + parseInt(this.hour.split(":")[1]);
    if (hourSmallMinutes < hourNowMinutes && hourNowMinutes < hourLargeMinutes) { return true; }
    else { return false; }
  }

  isWeekend() {
    var dayOfWeek = this.dateObj.getUTCDay();
    if (dayOfWeek > 5) { return true; }
    else { return false; }
  }


}
class TickerDTO {
  static schema = `${TickerDTO.name}/{tickerId}/{year}/{calendar}`;
  symbol = "";
  baseCurrency = "";
  toCurrency = "";
  open = 0;
  high = 0;
  low = 0;
  price = 0;  // Price/Close, Current price 
  candle = 0;
  timeStamp = 0;  // When last update time
  calendar = "";
  year = ""
  constructor(symbol = "", price = 0) {
    this.symbol = symbol.toUpperCase();;
    this.price = price;
    this.init();
  }
  init() {
    var myDate = new MyDate(timeZone.valueOf());
    this.timeStamp = myDate.timestamp; ///administor.firestore.FieldValue.serverTimestamp();
    this.baseCurrency = this.symbol.split("/")[0];
    this.toCurrency = this.symbol.split("/")[1];
    this.calendar = myDate.getCalendar();
    this.year = myDate.year.toString();
  }
  static reverseTicker(tickerList = [], callBackTickerList) {

    var returningTickerList = [];

    for (let index = 0; index < tickerList.length; index++) {
      var ticker = new TickerDTO(); ticker = tickerList[index];
      var returningTicker = new TickerDTO(ticker.toCurrency + "/" + ticker.baseCurrency, (1 / ticker.price));
      returningTicker.calendar = ticker.calendar;
      returningTicker.open = 1 / ticker.open;
      returningTicker.high = 1 / ticker.low;
      returningTicker.low = 1 / ticker.high;
      returningTicker.candle = returningTicker.price - returningTicker.open;
      returningTicker.calendar = ticker.calendar;
      returningTicker.year = ticker.year;
      /*-------------------------------*/
      returningTickerList.push(returningTicker);
    }
    callBackTickerList(returningTickerList);
  }
}
class CurrencyDTO {
  constructor(currencyCode = "USD") {
    this.code = currencyCode;
  }
  country = "";
  currency = "";
  code = "";
  symbol = "";
}

class Cryptos {
  constructor(name) {
    this.name = name
  }
  static getAllCoins(coinsCallBack) {
    coinsCallBack(Object.keys(Cryptos));
  }
  static isAny(symbol = "") {
    var result = false;
    var cryptos = Object.keys(Cryptos);
    for (var i = 0; i < cryptos.length; i++) {
      var cry = cryptos[i];
      //console.log(cry);
      if (cry == symbol) {
        result = true;
        break;
      }
    }
    return result;
  }
  // Create new instances of the same class as static attributes
  static BTC = new Cryptos("BTC")
  static ETH = new Cryptos("ETH")
  static USDT = new Cryptos("USDT")
  static BNB = new Cryptos("BNB")
  static XRP = new Cryptos("XRP")
  static ADA = new Cryptos("ADA")
  static SOL = new Cryptos("SOL")
  static DOGE = new Cryptos("DOGE")


}

class AbstractService {
  service;
  constructor(service) {
      this.service = service;
  }
  isParsable(stringResponse) {
      try {
          var parsedObject = JSON.parse(stringResponse);
          if (parsedObject && typeof parsedObject === "object") {
              return parsedObject;
          }
      } catch (e) {
          //H.errorLog(Service.name, "isParsable", e);
          return false;
      }

  }
  setGetRequest(url = "", response) {
      https.get(url, (res) => {
          if (200 > res.statusCode || res.statusCode > 300) {
              response(new ErrorResponse(url, res.statusMessage));
          }
          var responsedString = "";
          res.on('data', (d) => {
              responsedString += d;
          });

          res.on('end', function () {
              response(responsedString);
          });
      }).on('error', (err) => {
          response(new ErrorResponse(url + "    bu url cevap? error verdi!!??     ", err.message));
          //H.errorLog(DatabaseManager.name, "getTickers_currencyapi", err.message);
      });
  }

  setGetRequestHTTP(url = "", response) {
      http.get(url, (res) => {
          if (200 > res.statusCode || res.statusCode > 300) {
              response(new ErrorResponse(res.statusMessage));
          }
          var responsedString = "";
          res.on('data', (d) => {
              responsedString += d;
          });

          res.on('end', function () {
              response(responsedString);
          });
      }).on('error', (err) => {
          response(new ErrorResponse(err.message));
          H.errorLog(DatabaseManager.name, "getTickers_currencyapi", err.message);
      });
  }
  convertResponseToTickerlist(responsedString = "", callbackTickerList) { }
  getTickers(url = "", callbackTickerList) { }
  ProcessRequest(tickerListListener) { }
}
class Service_currencyapi extends AbstractService {
  updateInterval = "5 Minute";
  url = 'https://api.currencyapi.com/v2/latest?apikey=af16ae00-9416-11ec-84ba-c5e8c4460348&base_currency=USD';
  constructor(service = new AbstractService()) { super(service); }
  convertResponseToTickerlist(responsedString, callbackTickerList) {
      var tickerList = [];
      var result = this.isParsable(responsedString);

      if (result == false) {
          callbackTickerList(new ErrorResponse("Service_currencyapi", " stringResponse is  not Parsable!!"))
          return;
      }
      for (const property in result.data) {
          var symbol = property;
          var price = result.data[symbol];
          var ticker = new TickerDTO(baseCurrency + "/" + symbol, price);
          tickerList.push(ticker);
          //console.log(currency.symbol + "-->" + currency.price);

      }
      if (tickerList.length == 0) { callbackTickerList(new ErrorResponse("veri gelmedi...")) }
      else callbackTickerList(tickerList);
  }
  getTickers(url, callbackTickerList) {
      this.setGetRequest(url, (response) => {

          if (response instanceof ErrorResponse) {
              callbackTickerList(response);
              return;
          }
          this.convertResponseToTickerlist(response, (tickerList) => {
              callbackTickerList(tickerList);
          });
      });
  }
  ProcessRequest(tickerListListener) {
      this.getTickers(this.url, (tickerList) => {
          if (tickerList instanceof ErrorResponse) {
              if (this.service != null) {
                  this.service.ProcessRequest(tickerListListener);
              }

          }
          else {
              tickerListListener(tickerList);
          }
      });
  }
}
class Service_currencyapi2 extends AbstractService {
  updateInterval = "1 Hour";
  url = "https://currencyapi.net/api/v1/rates?key=4AcK5c5598q8LR9W6YWsTgRv2Pa9YuhicQKD&output=JSON";
  constructor(service = new AbstractService()) { super(service); }


  convertResponseToTickerlist(responsedString, callbackTickerList) {
      var tickerList = [];
      var result = this.isParsable(responsedString);
      if (!result) {
          return;
      }
      for (const property in result.rates) {
          var symbol = property;
          if (symbol == "USD") {
              continue;
          }
          var price = result.rates[property];
          var ticker = new TickerDTO(baseCurrency + "/" + symbol, price);
          tickerList.push(ticker);
      }
      if (tickerList.length == 0) { callbackTickerList(new ErrorResponse("veri gelmedi...")) }
      else callbackTickerList(tickerList);

  }
  getTickers(url, callbackTickerList) {
      this.setGetRequest(url, (responsedString) => {
          if (responsedString instanceof ErrorResponse) {
              return;
          }
          this.convertResponseToTickerlist(responsedString, (tickerList) => {
              callbackTickerList(tickerList);
          });
      });

  }
  ProcessRequest(tickerListListener) {
      this.getTickers(this.url, (tickerList) => {
          if (tickerList instanceof ErrorResponse) {
              if (this.service != null) {
                  this.service.ProcessRequest(tickerListListener);
              }

          }
          else {
              tickerListListener(tickerList);
          }
      });
  }

}
class Service_exchangerate extends AbstractService {
  updateInterval = "24 hour";
  url = " https://v6.exchangerate-api.com/v6/df61600aad0912e84b3e472c/latest/USD";
  constructor(service = new AbstractService()) { super(service); }
  convertResponseToTickerlist(responsedString, callbackTickerList) {
      var tickerList = [];
      var result = this.isParsable(responsedString);
      if (!result) {
          return;
      }
      for (const property in result.conversion_rates) {
          var symbol = property;
          if (symbol == "USD") {
              continue;
          }
          var price = result.conversion_rates[property];
          var ticker = new TickerDTO(baseCurrency + "/" + symbol, price);
          tickerList.push(ticker);
      }
      if (tickerList.length == 0) { callbackTickerList(new ErrorResponse("veri gelmedi...")) }
      else callbackTickerList(tickerList);

  }
  getTickers(url, callbackTickerList) {
      this.setGetRequest(url, (responsedString) => {
          if (responsedString instanceof ErrorResponse) {
              return;
          }
          this.convertResponseToTickerlist(responsedString, (tickerList) => {
              callbackTickerList(tickerList);
          });
      });

  }
  ProcessRequest(tickerListListener) {
      this.getTickers(this.url, (tickerList) => {
          if (tickerList instanceof ErrorResponse) {
              if (this.service != null) {
                  this.service.ProcessRequest(tickerListListener);
              }

          }
          else {
              tickerListListener(tickerList);
          }
      });
  }
}
class Service_blockChain extends AbstractService {
  url = "https://api.blockchain.com/v3/exchange/tickers";
  convertResponseToTickerlist(responsedString, callbackTickerList) {
      var tickerList = [];
      var result = this.isParsable(responsedString);

      if (result == false) {
          callbackTickerList(new ErrorResponse("Service_blockChain", " stringResponse is  not Parsable!!"))
          return;
      }
      for (var obj in result) {

          var symbol = (result[obj].symbol).replace("-", "/");
          var price = result[obj].last_trade_price;
          var ticker = new TickerDTO(symbol, price);
          tickerList.push(ticker);
      }
      if (tickerList.length == 0) { callbackTickerList(new ErrorResponse("veri gelmedi...")) }
      else callbackTickerList(tickerList);
  }


  getTickers(url, callbackTickerList) {
      this.setGetRequest(url, (response) => {

          if (response instanceof ErrorResponse) {
              callbackTickerList(response);
              return;
          }
          this.convertResponseToTickerlist(response, (tickerList) => {
              callbackTickerList(tickerList);
          });
      });
  }
  ProcessRequest(tickerListListener) {
      this.getTickers(this.url, (tickerList) => {
          if (tickerList instanceof ErrorResponse) {
              if (this.service != null) {
                  this.service.ProcessRequest(tickerListListener);
              }

          }
          else {
              tickerListListener(tickerList);
          }
      });
  }
}
class Service_Red_Alert extends AbstractService {
  ProcessRequest(tickerListListener) {
      // admine k?rm?z? alarm sinyali gönder. sms ile, bildirim ile, email ile.....
      throw new Error("veritaban? resfresh edilemedi!!!");
  }
}
class Service_CoinLayer extends AbstractService {
  url = 'http://api.coinlayer.com/live?access_key=b47bada31a79652e32362d08cfa7ec6c';
  constructor(service = new AbstractService()) { super(service); }
  convertResponseToTickerlist(responsedString, callbackTickerList) {
      var tickerList = [];
      var result = this.isParsable(responsedString);

      if (result == false) {
          callbackTickerList(new ErrorResponse(" stringResponse is  not Parsable!!"))
          return;
      }
      // yaz?lacak....
      for (const rate in result.rates) {

          var symbol = "USD" + "/" + rate.split(":")[0];
          var price = result.rates[rate];
          var price = 1 / price;


          if (Number.isFinite(price)) {
              var ticker = new TickerDTO(symbol, price);
              tickerList.push(ticker);
          }
      }
      if (tickerList.length == 0) {
          callbackTickerList(new ErrorResponse("veri gelmedi..."))
      }
      else callbackTickerList(tickerList);
  }

  getTickers(url, callbackTickerList) {
      this.setGetRequestHTTP(url, (response) => {
          if (response instanceof ErrorResponse) {
              callbackTickerList(response);
              return;
          }
          this.convertResponseToTickerlist(response, (tickerList) => {
              callbackTickerList(tickerList);
          });
      });
  }
  ProcessRequest(tickerListListener) {
      this.getTickers(this.url, (tickerList) => {
          if (tickerList instanceof ErrorResponse) {
              if (this.service != null) {
                  this.service.ProcessRequest(tickerListListener);
              }

          }
          else {
              tickerListListener(tickerList);
          }
      });
  }
}
class Service_CoinLayer2 extends AbstractService {
  apiKey = '66303ea578814beddf06c9016e9754bb';
  url = 'http://api.coinlayer.com/live?access_key=' + this.apiKey;
  constructor(service = new AbstractService()) { super(service); }
  convertResponseToTickerlist(responsedString, callbackTickerList) {
      var tickerList = [];
      var result = this.isParsable(responsedString);

      if (result == false) {
          callbackTickerList(new ErrorResponse(" stringResponse is  not Parsable!!"))
          return;
      }
      // yaz?lacak....
      for (const rate in result.rates) {
          var symbol = "USD" + "/" + rate.split(":")[0];
          var price = result.rates[rate];
          var price = 1 / price;


          if (Number.isFinite(price)) {
              var ticker = new TickerDTO(symbol, price);
              tickerList.push(ticker);
          }

      }
      if (tickerList.length == 0) {
          callbackTickerList(new ErrorResponse("veri gelmedi..."))
      }
      else callbackTickerList(tickerList);
  }

  getTickers(url, callbackTickerList) {
      this.setGetRequestHTTP(url, (response) => {
          if (response instanceof ErrorResponse) {
              callbackTickerList(response);
              return;
          }
          this.convertResponseToTickerlist(response, (tickerList) => {
              callbackTickerList(tickerList);
          });
      });
  }
  ProcessRequest(tickerListListener) {
      this.getTickers(this.url, (tickerList) => {
          if (tickerList instanceof ErrorResponse) {
              if (this.service != null) {
                  this.service.ProcessRequest(tickerListListener);
              }

          }
          else {
              tickerListListener(tickerList);
          }
      });
  }
}
class Service_CoinLayer3 extends AbstractService {
  account = {
      mail: 'onur.kaymak....@gmail.com',
      passwd: '.371....'
  }
  apiKey = 'ca88987759f8df7b447a94447bf72854';
  url = 'http://api.coinlayer.com/live?access_key=' + this.apiKey;
  constructor(service = new AbstractService()) { super(service); }
  convertResponseToTickerlist(responsedString, callbackTickerList) {
      var tickerList = [];
      var result = this.isParsable(responsedString);

      if (result == false) {
          callbackTickerList(new ErrorResponse(" stringResponse is  not Parsable!!"))
          return;
      }
      // yaz?lacak....
      for (const rate in result.rates) {
          var symbol = "USD" + "/" + rate.split(":")[0];
          var price = result.rates[rate];
          var price = 1 / price;


          if (Number.isFinite(price)) {
              var ticker = new TickerDTO(symbol, price);
              tickerList.push(ticker);
          }

      }
      if (tickerList.length == 0) {
          callbackTickerList(new ErrorResponse("veri gelmedi..."))
      }
      else callbackTickerList(tickerList);
     
  }

  getTickers(url, callbackTickerList) {
      this.setGetRequestHTTP(url, (response) => {
          if (response instanceof ErrorResponse) {
              callbackTickerList(response);
              return;
          }
          this.convertResponseToTickerlist(response, (tickerList) => {
              callbackTickerList(tickerList);
          });
      });
  }
  ProcessRequest(tickerListListener) {
      this.getTickers(this.url, (tickerList) => {
          if (tickerList instanceof ErrorResponse) {
              if (this.service != null) {
                  this.service.ProcessRequest(tickerListListener);
              }

          }
          else {
              tickerListListener(tickerList);
          }
      });
  }
}
class Service_CoinLayer4 extends AbstractService {
  account = {
      mail: 'kaymak.serkan....@gmail.com',
      passwd: '.y....!!'
  }
  apiKey = 'c5fac34c97ad8c254fdc38ae9f794227';
  url = 'http://api.coinlayer.com/live?access_key=' + this.apiKey;
  constructor(service = new AbstractService()) { super(service); }
  convertResponseToTickerlist(responsedString, callbackTickerList) {
      var tickerList = [];
      var result = this.isParsable(responsedString);

      if (result == false) {
          callbackTickerList(new ErrorResponse(" stringResponse is  not Parsable!!"))
          return;
      }
      // yaz?lacak....
      for (const rate in result.rates) {
          var symbol = "USD" + "/" + rate.split(":")[0];
          var price = result.rates[rate];
          var price = 1 / price;


          if (Number.isFinite(price)) {
              var ticker = new TickerDTO(symbol, price);
              tickerList.push(ticker);
          }

      }
      if (tickerList.length==0) {
          callbackTickerList(new ErrorResponse("veri gelmedi..."))
      }
      else callbackTickerList(tickerList);
  }

  getTickers(url, callbackTickerList) {
      this.setGetRequestHTTP(url, (response) => {
          if (response instanceof ErrorResponse) {
              callbackTickerList(response);
              return;
          }
          this.convertResponseToTickerlist(response, (tickerList) => {
              callbackTickerList(tickerList);
          });
      });
  }
  ProcessRequest(tickerListListener) {
      this.getTickers(this.url, (tickerList) => {
          if (tickerList instanceof ErrorResponse) {
              if (this.service != null) {
                  this.service.ProcessRequest(tickerListListener);
              }

          }
          else {
              tickerListListener(tickerList);
          }
      });
  }
}
class Service_fscApi extends AbstractService {
  url = 'https://fcsapi.com/api-v3/forex/history?symbol=USD/ETH&period=1h&access_key=SmsOjuhdCSvAFzL59ELjVmE';
  convertResponseToTickerlist(responsedString, callbackTickerList) {
      var tickerList = [];
      var result = this.isParsable(responsedString);

      if (result == false) {
          callbackTickerList(new ErrorResponse("Service_fscApi", " stringResponse is  not Parsable!!"))
          return;
      }
      var dataObj = result.response;
      var symbol = result.info.symbol;
      for (const property in dataObj) {
          //console.log(dataObj[property]);
          var obj = dataObj[property];
          var ticker = new TickerDTO(symbol, 0);
          ticker.symbol = symbol;
          ticker.baseCurrency = symbol.split("/")[0];
          ticker.toCurrency = symbol.split("/")[1];
          ticker.price = parseFloat(obj.c);
          ticker.low = parseFloat(obj.l);
          ticker.high = parseFloat(obj.h);
          ticker.open = parseFloat(obj.o);
          ticker.timeStamp = obj.t;
          ticker.candle = ticker.price - ticker.open;
          var calendar = obj.tm.split(" ")[0];
          ticker.calendar = calendar.split("-")[0] + "-" + calendar.split("-")[1] + "-" + calendar.split("-")[2];
          ticker.year = calendar.split("-")[0];
          /-------------------------------------*/
          tickerList.push(ticker);
          //console.log(ticker);
      }
      //console.log(tickerList);
      if (tickerList.length == 0) {callbackTickerList(new ErrorResponse("veri gelmedi..."))  }
      else callbackTickerList(tickerList);
  }

  getTickers(url, callbackTickerList) {
      this.setGetRequest(url, (response) => {
          if (response instanceof ErrorResponse) {
              callbackTickerList(response);
              return;
          }
          this.convertResponseToTickerlist(response, (tickerList) => {
              callbackTickerList(tickerList);
          });
      });
  }

  ProcessRequest(tickerListListener) {
      this.getTickers(this.url, (tickerList) => {
          if (tickerList instanceof ErrorResponse) {
              if (this.service != null) {
                  this.service.ProcessRequest(tickerListListener);
              }

          }
          else {
              tickerListListener(tickerList);
          }
      });
  }
}

class SeedDatabase {

  seed_Usd_Btc(callBackTickerList) {
    var service = new Service_fscApi(null);
    service.getTickers('https://fcsapi.com/api-v3/forex/history?symbol=BTC/USD&period=1d&access_key=SmsOjuhdCSvAFzL59ELjVmE', tickerList => {
      TickerDTO.reverseTicker(tickerList, tickerListR => {
        callBackTickerList(tickerListR);
      })

    });
  }
  seed_Usd_ETH(callBackTickerList) {
    var service = new Service_fscApi(null);
    service.getTickers('https://fcsapi.com/api-v3/forex/history?symbol=ETH/USD&period=1d&access_key=SmsOjuhdCSvAFzL59ELjVmE', tickerList => {
      TickerDTO.reverseTicker(tickerList, tickerListR => {
        callBackTickerList(tickerListR);
      })
    });
  }
  seed_Usd_USDT(callBackTickerList) {
    var service = new Service_fscApi(null);
    service.getTickers('https://fcsapi.com/api-v3/forex/history?symbol=USDT/USD&period=1d&access_key=SmsOjuhdCSvAFzL59ELjVmE', tickerList => {
      TickerDTO.reverseTicker(tickerList, tickerListR => {
        callBackTickerList(tickerListR);
      })
    });
  }
  seed_Usd_XRP(callBackTickerList) {
    var service = new Service_fscApi(null);
    var url = 'https://fcsapi.com/api-v3/forex/history?symbol=XRP/USD&period=1d&access_key=SmsOjuhdCSvAFzL59ELjVmE';
    service.getTickers(url, tickerList => {
      TickerDTO.reverseTicker(tickerList, tickerListR => {
        callBackTickerList(tickerListR);
      })
    });
  }
  seed_Usd_ADA(callBackTickerList) {
    var service = new Service_fscApi(null);
    var url = 'https://fcsapi.com/api-v3/forex/history?symbl=ADA/USD&period=1d&access_key=SmsOjuhdCSvAFzL59ELjVmE';
    service.getTickers(url, tickerList => {
      TickerDTO.reverseTicker(tickerList, tickerListR => {
        callBackTickerList(tickerListR);
      })
    });
  }
  seed_Usd_SOL(callBackTickerList) {
    var service = new Service_fscApi(null);
    var url = 'https://fcsapi.com/api-v3/forex/history?symbol=SOL/USD&period=1d&access_key=SmsOjuhdCSvAFzL59ELjVmE';
    service.getTickers(url, tickerList => {
      TickerDTO.reverseTicker(tickerList, tickerListR => {
        callBackTickerList(tickerListR);
      })
    });
  }
  seed_Usd_DOGE(callBackTickerList) {
    var service = new Service_fscApi(null);
    var url = 'https://fcsapi.com/api-v3/forex/history?symbol=DOGE/USD&period=1d&access_key=SmsOjuhdCSvAFzL59ELjVmE';
    service.getTickers(url, tickerList => {
      TickerDTO.reverseTicker(tickerList, tickerListR => {
        callBackTickerList(tickerListR);
      })
    });

  }
  seed_Currencies() {
    var count = 0;
    let batch = administor.firestore().batch();
    var service = new Service_currencyapi(null);
    service.ProcessRequest(tickerList => {
      var currencyDTOList = [new CurrencyDTO("USD")];
      tickerList.forEach((ticker) => {
        count = count + 1;
        var currencyDTO = new CurrencyDTO(ticker.toCurrency);
        currencyDTOList.push(currencyDTO);
        const documentRef = administor.firestore().collection(CurrencyDTO.name).doc(currencyDTO.code);
        const data = {
          country: "",
          currency: "",
          code: currencyDTO.code,
          symbol: ""
        }
        batch.set(documentRef, data, { merge: true });
        if (count >= tickerList.length) { batch.commit(); } // async methodunun icinde ollmal?!!
      });

    });

  }
  seed_Coin_Currencies() {
    var count = 0;
    let batch = administor.firestore().batch();
    var service = new Service_CoinLayer2(null);
    service.ProcessRequest(returningtickerList => {
      var tickerList = [];
      returningtickerList.forEach(ticker => {
        if (Cryptos.isAny(ticker.toCurrency)) {
          tickerList.push(ticker);
        }
      });
      var currencyDTOList = [new CurrencyDTO("USD")];
      tickerList.forEach((ticker) => {
        count = count + 1;
        var currencyDTO = new CurrencyDTO(ticker.toCurrency);
        currencyDTOList.push(currencyDTO);
        const documentRef = administor.firestore().collection(CurrencyDTO.name).doc(currencyDTO.code);
        const data = {
          country: "",
          currency: "",
          code: currencyDTO.code,
          symbol: ""
        }
        batch.set(documentRef, data, { merge: true });
        if (count >= tickerList.length) { batch.commit(); } // async methodunun icinde ollmal?!!
      });

    });

  }
}

class DatabaseManager {

  validateTicker(ticker = new TickerDTO("", 0)) {
    var result = true;
    if (ticker.calendar.length != 10) result = false;
    if ((ticker.baseCurrency + "-" + ticker.toCurrency) == undefined) result = false;
    if (ticker.baseCurrency == "" || ticker.toCurrency == "") result = false;
    /*-------------------------------------------------------------------------------------*/
    if (result == false) H.errorLog(DatabaseManager.name, 'validateTicker', ticker);
    return result;
  }
  setTicker(ticker = new TickerDTO("", ""), mergeValue = true) {
    var isValid = this.validateTicker(ticker);
    if (!isValid) {
      return;
      H.errorLog(DatabaseManager.name, "validateTicker", "ticker is nat valid ");
    }
    const data = {
      symbol: ticker.symbol,
      price: ticker.price,  // Price/Close, Current price 
      timeStamp: ticker.timeStamp,//administor.firestore.FieldValue.serverTimestamp(),// When last update time
      calendar: ticker.calendar
    }
    if (ticker.open != 0) {
      data.open = ticker.open;
    }
    if (ticker.high != 0) {
      data.high = ticker.high;
    }
    if (ticker.low != 0) {
      data.low = ticker.low;
    }
    if (ticker.candle != 0) {
      data.candle = ticker.candle;
    }
    // merge:true --> eski field lar kalsın. silinmesin demek!
    const document = administor.firestore().collection(TickerDTO.name).doc(ticker.baseCurrency + "-" + ticker.toCurrency).collection(ticker.year).doc(ticker.calendar);
    document.set(data, { merge: mergeValue }).then((result) => {
      //H.infoLog(DatabaseManager.name, "setTicker", ticker.symbol + "successfully set");
    }).catch(err => {
      //H.errorLog(DatabaseManager.name, "setTicker", err);
    });

  }

  setTickersWithPromise(tickerList = [ticker = new TickerDTO("", "")], timeout) {

    //does not work
    tickerList.forEach(ticker => {

      var promise = new Promise(resolve => {
        const document = administor.firestore().collection(TickerDTO.name).doc(ticker.baseCurrency + "-" + ticker.toCurrency).collection(ticker.year).doc(ticker.date);
        document.set({
          symbol: ticker.symbol,
          price: ticker.price,  // Price/Close, Current price 
          timeStamp: ticker.timeStamp,//administor.firestore.FieldValue.serverTimestamp(),// When last update time
          calendar: ticker.calendar,
          year: ticker.year,
        }).then((result) => {
          //H.infoLog(DatabaseManager.name, "setTicker", ticker.symbol + "succescully set");

        }).catch(err => {
          //H.errorLog(DatabaseManager.name, "setTicker", ticker.symbol + err);
        });

        setTimeout(resolve, timeout);
      });

    });



  }

  setTickers(tickerList = [new TickerDTO("", "")], writeAtTime = 0, mergeValue = true) {
    let batch = administor.firestore().batch();
    let count = 0;

    tickerList.forEach(ticker => {
      var isValid = this.validateTicker(ticker);
      if (!isValid) {
        return;
      }

      const documentRef = administor.firestore().collection(TickerDTO.name).doc(ticker.baseCurrency + "-" + ticker.toCurrency).collection(ticker.year).doc(ticker.calendar);

      const data = {
        symbol: ticker.symbol,
        price: ticker.price,  // Price/Close, Current price 
        timeStamp: ticker.timeStamp,//administor.firestore.FieldValue.serverTimestamp(),// When last update time
        calendar: ticker.calendar
      }
      if (ticker.open != 0) { data.open = ticker.open; }
      if (ticker.high != 0) { data.high = ticker.high; }
      if (ticker.low != 0) { data.low = ticker.low; }
      if (ticker.candle != 0) { data.candle = ticker.candle; }
      batch.set(documentRef, data, { merge: mergeValue });
      count = count + 1;
      if (count >= writeAtTime || count >= tickerList.length) {
        H.infoLog("DatabaseManager" + "setTickers" + "batches is  commiting....");
        batch.commit().then(writeResult => {
          H.infoLog("DatabaseManager" + "setTickers" + "batches is successfully committed.");
          batch = administor.firestore().batch();
          count = 0;
        });
      }
    });
  }
  createTicker(ticker = new TickerDTO("", "")) {
    const document = administor.firestore().collection(TickerDTO.name).doc(ticker.baseCurrency + "-" + ticker.toCurrency + "-" + ticker.calendar);
    document.create({
      symbol: ticker.symbol,
      price: ticker.price,  // Price/Close, Current price 
      timeStamp: ticker.timeStamp, // When last update time
      calendar: ticker.calendar,
      year: ticker.year,
    }).then((result) => {
      //H.infoLog(DatabaseManager.name, "createTicker", result);
    }).catch(err => {
      //H.infoLog(DatabaseManager.name, "createTicker", err);

    });
  }
  readById(documentId = "", collectionName = "", callBack) {
    const document = administor.firestore().doc(`/${collectionName}/${documentId}`).get().then((dataObj) => {
      callBack(dataObj);
    });
  }
  readByQuery(pathName = "", pathValue = "", filterOperation = "==", collectionName = "", callBack) {
    const document = administor.firestore().collection(collectionName).where(pathName, filterOperation, pathValue).get().then((dataList) => {
      callBack(dataList);
    })
  }
}


exports.onCreateTicker = functions.firestore.document('TickerDTO/{tickerId}/{year}/{calendar}')
  .onCreate((snap, context) => {
    const data = snap.data();
    if (!data.low) { data.low = data.price; }
    if (!data.open) { data.open = data.price; }
    if (!data.high) { data.high = data.price; }
    data.candle = data.price - data.open;
    //H.infoLog("DataManager", "onCreate()", "low : " + data.low + "open : " + data.open + "high : " + data.high + "candle : " + data.candle);
    return snap.ref.set(data, { merge: true });
  });

exports.onUpdateTicker = functions
  .runWith({
    timeoutSeconds: 60,
    memory: "256MB",
  })
  .firestore.document('TickerDTO/{tickerId}/{year}/{calendar}')
  .onUpdate((change, context) => {
    const data = change.after.data();
    const previousData = change.before.data();
    if (data.price == previousData.price) {
      // H.infoLog("", "onUpdate()", "price is same!! returning...");
      return null;
    }
    // functions.logger.debug("old ->" + previousData.symbol + "new ->" + data.symbol);
    // functions.logger.debug("old price : " + previousData.price + "new  price : " + data.price);
    // functions.logger.debug(!previousData || data.price == previousData.price);
    if (previousData.high < data.price) { data.high = data.price; data.low = previousData.low; }
    //functions.logger.debug("new high->" + data.high);
    if (previousData.low > data.price) { data.low = data.price; data.high = previousData.high; }
    data.open = previousData.open;
    data.candle = data.price - data.open;
    //H.infoLog("DataManager", "onUpdate()", "previousData : " + Object.keys(previousData));
    //H.infoLog("DataManager", "onUpdate()", "newData : " + Object.keys(data));
    // data.open = previousData.open;
    return change.after.ref.set(data, { merge: true });
  });
exports.refreshTickers = functions
  .runWith({
    timeoutSeconds: 60,
    memory: "256MB",
  })
  .pubsub.schedule('every 180 minutes')
  .onRun((context) => {
    /*----------------------------------------------*/
    var myDate = new MyDate("Londra");
    if (myDate.isWeekend()) {
      //H.infoLog("myDate.isWeekend-- >" + " hafta sonu!! borsa kapalı!! ");
      //return "hafta sonu!!";
    }
    if (myDate.isTimeBetween("09:00", "17:00")) {
      //H.infoLog("myDate.isTimeBetween-- >" + "09:00 - 20:00 disindayiz...borsa kapalı!! ");
      //return " borsa kapalı!! ";
    }
    /*-----------------------------------------------*/
    var databaseManager = new DatabaseManager();

    var service_Red_Alert = new Service_Red_Alert(null);
    var exchangerateService = new Service_exchangerate(service_Red_Alert);
    var currencApiService = new Service_currencyapi(exchangerateService);
    var currencApiService2 = new Service_currencyapi2(currencApiService);

    var coinLayer1 = new Service_CoinLayer(service_Red_Alert);
    var coinLayer2 = new Service_CoinLayer2(coinLayer1);
    var coinLayer3 = new Service_CoinLayer3(coinLayer2);
    var coinLayer4 = new Service_CoinLayer4(coinLayer3);


    currencApiService2.ProcessRequest(tickerList1 => {
      coinLayer4.ProcessRequest(tickerList2 => {

        var tickerList = [];
        tickerList1.forEach(t => { tickerList.push(t); });
        tickerList2.forEach(t => { tickerList.push(t); });
        console.log(tickerList.length + 'kadar ticker veritabanına yazılıyor...');
        databaseManager.setTickers(tickerList, 500, true);

      });
    });
    /*-----------------------------------------------------*/
    return "refresh method executed!";
  });


exports.testMethod = functions.https.onRequest((request, response) => {
  var databaseManager = new DatabaseManager();
  var seedDatabase = new SeedDatabase();
  seedDatabase.seed_Usd_XRP(tickerList => {
    databaseManager.setTickers(tickerList, 500, true);
  })
});


exports.seed_Usd_XRP = functions.https.onRequest((request, response) => {
  var databaseManager = new DatabaseManager();
  var seedDatabase = new SeedDatabase();
  seedDatabase.seed_Usd_XRP(tickerList => {
    databaseManager.setTickers(tickerList, 500, true);
  })
});

exports.seed_Usd_ADA = functions.https.onRequest((request, response) => {
  var databaseManager = new DatabaseManager();
  var seedDatabase = new SeedDatabase();
  seedDatabase.seed_Usd_ADA(tickerList => {
    databaseManager.setTickers(tickerList, 500, true);
  })
});
exports.seed_Usd_Btc = functions.https.onRequest((request, response) => {
  var databaseManager = new DatabaseManager();
  var seedDatabase = new SeedDatabase();
  seedDatabase.seed_Usd_Btc(tickerList => {
    databaseManager.setTickers(tickerList, 500, true);
  })
});
exports.seed_Usd_DOGE = functions.https.onRequest((request, response) => {
  var databaseManager = new DatabaseManager();
  var seedDatabase = new SeedDatabase();
  seedDatabase.seed_Usd_DOGE(tickerList => {
    databaseManager.setTickers(tickerList, 500, true);
  })
});
exports.seed_Usd_ETH = functions.https.onRequest((request, response) => {
  var databaseManager = new DatabaseManager();
  var seedDatabase = new SeedDatabase();
  seedDatabase.seed_Usd_ETH(tickerList => {
    databaseManager.setTickers(tickerList, 500, true);
  })
});
exports.seed_Usd_SOL = functions.https.onRequest((request, response) => {
  var databaseManager = new DatabaseManager();
  var seedDatabase = new SeedDatabase();
  seedDatabase.seed_Usd_SOL(tickerList => {
    databaseManager.setTickers(tickerList, 500, true);
  })
});
exports.seed_Usd_USDT = functions.https.onRequest((request, response) => {
  var databaseManager = new DatabaseManager();
  var seedDatabase = new SeedDatabase();
  seedDatabase.seed_Usd_USDT(tickerList => {
    databaseManager.setTickers(tickerList, 500, true);
  })
});

exports.seed_Usd_XRP = functions.https.onRequest((request, response) => {
  var databaseManager = new DatabaseManager();
  var seedDatabase = new SeedDatabase();
  seedDatabase.seed_Usd_XRP(tickerList => {
    databaseManager.setTickers(tickerList, 500, true);
  })
});


exports.sendNotificationToAdmin = functions.https.onRequest((req, res) => {


});
/*-----------------------------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------------------------------*/

/*-----------------------  wait for press key to exit from console  -----------------------------------*/
//console.log("wait..");
//require('readline')
//    .createInterface(process.stdin, process.stdout)
//    .question("Press [Enter] to exit...", function () {
//        process.exit();
//    });