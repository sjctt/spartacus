
import {withParams, regex } from "vuelidate/lib/validators/common.js"
export var passwordRex =regex('passwordRex',/^[a-zA-Z0-9]{4,16}$/);
export var positiveNum=regex('positiveNum',/^[0-9]*[1-9][0-9]*$/);
export var phoneNumRex=regex('phoneNumRex', /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/);  
export var ipRex=regex('ipRex', /^((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))$/);  