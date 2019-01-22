if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'frontend'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'frontend'.");
}
var frontend = function (_, Kotlin) {
  'use strict';
  var equals = Kotlin.equals;
  var Kind_OBJECT = Kotlin.Kind.OBJECT;
  var Enum = Kotlin.kotlin.Enum;
  var Kind_CLASS = Kotlin.Kind.CLASS;
  var throwISE = Kotlin.throwISE;
  var throwCCE = Kotlin.throwCCE;
  var toString = Kotlin.toString;
  var Unit = Kotlin.kotlin.Unit;
  ApiEndpoint.prototype = Object.create(Enum.prototype);
  ApiEndpoint.prototype.constructor = ApiEndpoint;
  function ApiEndpoint(name, ordinal) {
    Enum.call(this);
    this.name$ = name;
    this.ordinal$ = ordinal;
  }
  function ApiEndpoint_initFields() {
    ApiEndpoint_initFields = function () {
    };
    ApiEndpoint$UPLOAD_instance = new ApiEndpoint('UPLOAD', 0);
    ApiEndpoint$Companion_getInstance();
  }
  var ApiEndpoint$UPLOAD_instance;
  function ApiEndpoint$UPLOAD_getInstance() {
    ApiEndpoint_initFields();
    return ApiEndpoint$UPLOAD_instance;
  }
  ApiEndpoint.prototype.path = function () {
    if (equals(this, ApiEndpoint$UPLOAD_getInstance()))
      return ApiEndpoint$Companion_getInstance().ROOT + ApiEndpoint$Companion_getInstance().REL_API + ApiEndpoint$Companion_getInstance().REL_UPLOAD;
    else
      return Kotlin.noWhenBranchMatched();
  };
  function ApiEndpoint$Companion() {
    ApiEndpoint$Companion_instance = this;
    this.ROOT = 'http://localhost:8080';
    this.REL_API = '/api';
    this.REL_UPLOAD = '/upload';
  }
  ApiEndpoint$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var ApiEndpoint$Companion_instance = null;
  function ApiEndpoint$Companion_getInstance() {
    ApiEndpoint_initFields();
    if (ApiEndpoint$Companion_instance === null) {
      new ApiEndpoint$Companion();
    }
    return ApiEndpoint$Companion_instance;
  }
  ApiEndpoint.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'ApiEndpoint',
    interfaces: [Enum]
  };
  function ApiEndpoint$values() {
    return [ApiEndpoint$UPLOAD_getInstance()];
  }
  ApiEndpoint.values = ApiEndpoint$values;
  function ApiEndpoint$valueOf(name) {
    switch (name) {
      case 'UPLOAD':
        return ApiEndpoint$UPLOAD_getInstance();
      default:throwISE('No enum constant ApiEndpoint.' + name);
    }
  }
  ApiEndpoint.valueOf_61zpoe$ = ApiEndpoint$valueOf;
  function ApiResult(result, success, message) {
    if (success === void 0)
      success = true;
    if (message === void 0)
      message = '';
    this.result = result;
    this.success = success;
    this.message = message;
  }
  ApiResult.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'ApiResult',
    interfaces: []
  };
  ApiResult.prototype.component1 = function () {
    return this.result;
  };
  ApiResult.prototype.component2 = function () {
    return this.success;
  };
  ApiResult.prototype.component3 = function () {
    return this.message;
  };
  ApiResult.prototype.copy_at9idu$ = function (result, success, message) {
    return new ApiResult(result === void 0 ? this.result : result, success === void 0 ? this.success : success, message === void 0 ? this.message : message);
  };
  ApiResult.prototype.toString = function () {
    return 'ApiResult(result=' + Kotlin.toString(this.result) + (', success=' + Kotlin.toString(this.success)) + (', message=' + Kotlin.toString(this.message)) + ')';
  };
  ApiResult.prototype.hashCode = function () {
    var result = 0;
    result = result * 31 + Kotlin.hashCode(this.result) | 0;
    result = result * 31 + Kotlin.hashCode(this.success) | 0;
    result = result * 31 + Kotlin.hashCode(this.message) | 0;
    return result;
  };
  ApiResult.prototype.equals = function (other) {
    return this === other || (other !== null && (typeof other === 'object' && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.result, other.result) && Kotlin.equals(this.success, other.success) && Kotlin.equals(this.message, other.message)))));
  };
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
        var result = JSON.parse(toString(this$.response));
        draw(result);
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
        request.open('POST', ApiEndpoint$UPLOAD_getInstance().path());
        request.onreadystatechange = main$lambda$lambda$lambda$lambda(request, request);
        request.send(formData);
      }
      return Unit;
    };
  }
  function main(args) {
    var tmp$, tmp$_0;
    var inputElement = Kotlin.isType(tmp$ = document.getElementById('filePicker'), HTMLInputElement) ? tmp$ : throwCCE();
    var button = Kotlin.isType(tmp$_0 = document.getElementById('predictButton'), HTMLButtonElement) ? tmp$_0 : throwCCE();
    button.addEventListener('click', main$lambda(inputElement));
  }
  function draw($receiver) {
    var tmp$, tmp$_0;
    var image = Kotlin.isType(tmp$ = document.getElementById('digitImage'), HTMLImageElement) ? tmp$ : throwCCE();
    var text = Kotlin.isType(tmp$_0 = document.getElementById('predictionText'), HTMLParagraphElement) ? tmp$_0 : throwCCE();
    if ($receiver.success) {
      image.src = $receiver.result.imageURL;
      text.innerText = 'Is this ' + $receiver.result.prediction + ' ?';
    }
     else {
      image.src = 'http://localhost:8080/static/question_mark.png';
      text.innerText = $receiver.message;
    }
  }
  Object.defineProperty(ApiEndpoint, 'UPLOAD', {
    get: ApiEndpoint$UPLOAD_getInstance
  });
  Object.defineProperty(ApiEndpoint, 'Companion', {
    get: ApiEndpoint$Companion_getInstance
  });
  _.ApiEndpoint = ApiEndpoint;
  _.ApiResult = ApiResult;
  _.PredictionResult = PredictionResult;
  _.main_kand9s$ = main;
  _.draw_xpc3uq$ = draw;
  main([]);
  Kotlin.defineModule('frontend', _);
  return _;
}(typeof frontend === 'undefined' ? {} : frontend, kotlin);
