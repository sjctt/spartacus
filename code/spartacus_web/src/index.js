
import "./assets/css/vendor/dropzone.min.css";
import "./assets/css/vendor/bootstrap.min.css";
/*  if you want use single color theme : 

- ColorSwitcher component remove in 'src/App.vue' file.
- Remove multicolor section in current file
- Uncomment the two lines below 

import "assets/css/sass/themes/piaf.light.orange.scss";
import "./main";
*/


/* if you want single color remove this section:multicolor*/
var color = 'light.blue';
if (localStorage.getItem('themeColor')) {
  color = localStorage.getItem('themeColor');
}
let render = () => {
    const css = import('./assets/css/sass/themes/piaf.' + color + '.scss')
    .then(x => {
      const MainApp = require('./main');
    })
};
render();

/* if you want single color remove this section:multicolor*/