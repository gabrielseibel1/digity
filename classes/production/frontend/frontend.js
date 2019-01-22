if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'frontend'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'frontend'.");
}
var frontend = function (_, Kotlin) {
  'use strict';
  var Kind_CLASS = Kotlin.Kind.CLASS;
  var throwCCE = Kotlin.throwCCE;
  var toString = Kotlin.toString;
  var Unit = Kotlin.kotlin.Unit;
  function PredictionResult(prediction, imageURL) {
    this.prediction = prediction;
    this.imageURL = imageURL;
  }
  PredictionResult.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'PredictionResult',
    interfaces: []
  };
  PredictionResult.prototype.component1 = function () {
    return this.prediction;
  };
  PredictionResult.prototype.component2 = function () {
    return this.imageURL;
  };
  PredictionResult.prototype.copy_19mbxw$ = function (prediction, imageURL) {
    return new PredictionResult(prediction === void 0 ? this.prediction : prediction, imageURL === void 0 ? this.imageURL : imageURL);
  };
  PredictionResult.prototype.toString = function () {
    return 'PredictionResult(prediction=' + Kotlin.toString(this.prediction) + (', imageURL=' + Kotlin.toString(this.imageURL)) + ')';
  };
  PredictionResult.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.prediction) | 0;
    result = result * 31 + Kotlin.hashCode(this.imageURL) | 0;
    return result;
  };
  PredictionResult.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.prediction, other.prediction) && Kotlin.equals(this.imageURL, other.imageURL)))));
  };
  function main$lambda$lambda$lambda$lambda(closure$request, this$) {
    return function (it) {
      if (closure$request.readyState === XMLHttpRequest.DONE && closure$request.status === 200) {
        var predictionResult = JSON.parse(toString(this$.response));
        draw(predictionResult);
      }
      return Unit;
    };
  }
  function main$lambda(closure$inputElement) {
    return function (it) {
      var tmp$, tmp$_0;
      if ((tmp$_0 = (tmp$ = closure$inputElement.files) != null ? tmp$[0] : null) != null) {
        var formData = new FormData();
        formData.append('file', tmp$_0);
        var request = new XMLHttpRequest();
        request.open('POST', 'http://localhost:8080');
        request.onreadystatechange = main$lambda$lambda$lambda$lambda(request, request);
        request.send(formData);
      }
      return Unit;
    };
  }
  function main(args) {
    var tmp$, tmp$_0;
    var inputElement = Kotlin.isType(tmp$ = document.getElementById('myFileField'), HTMLInputElement) ? tmp$ : throwCCE();
    var button = Kotlin.isType(tmp$_0 = document.getElementById('myButton'), HTMLButtonElement) ? tmp$_0 : throwCCE();
    button.addEventListener('click', main$lambda(inputElement));
  }
  function draw($receiver) {
    var tmp$, tmp$_0;
    var image = Kotlin.isType(tmp$ = document.getElementById('digitImage'), HTMLImageElement) ? tmp$ : throwCCE();
    var text = Kotlin.isType(tmp$_0 = document.getElementById('predictionText'), HTMLParagraphElement) ? tmp$_0 : throwCCE();
    image.src = $receiver.imageURL;
    text.innerText = 'Is this ' + $receiver.prediction + ' ?';
  }
  _.PredictionResult = PredictionResult;
  _.main_kand9s$ = main;
  _.draw_q7xs77$ = draw;
  main([]);
  Kotlin.defineModule('frontend', _);
  return _;
}(typeof frontend === 'undefined' ? {} : frontend, kotlin);
