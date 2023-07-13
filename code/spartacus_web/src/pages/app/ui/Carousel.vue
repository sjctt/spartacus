<template>
  <div>    
    <b-row>
      <b-colxx xxs="12">
        <piaf-breadcrumb :heading="$t('menu.carousel')"/>
        <div class="separator mb-5"></div>
      </b-colxx>
    </b-row>
    <b-row>
      <b-colxx xxs="12">
        <h5 class="mb-4 card-title">{{ $t('carousel.basic') }}</h5>
      </b-colxx>
      <b-colxx xxs="12" class="mb-4 pl-0 pr-0">
        <swiper :options="swiperBasicOption" ref="swiperBasic">
          <swiper-slide> 
            <div class="pr-3 pl-3">
              <b-card class="flex-row" no-body>
              <div class="w-50 position-relative">
                <img class="card-img-left" src="/assets/img/card-thumb-1.jpg" alt="Card cap" />
                <b-badge variant="primary" pill class="position-absolute badge-top-left">NEW</b-badge>
              </div>
              <div class="w-50">
                <b-card-body>
                    <h6 class="mb-4 card-subtitle">Cheesecake with Fresh Berries and Mint</h6>
                    <p class="card-text text-muted text-small mb-0 font-weight-light">09.04.2018</p>
                </b-card-body>
              </div>
            </b-card>
          </div>
          </swiper-slide>
          <swiper-slide> 
            <div class="pr-3 pl-3">
                <b-card class="flex-row" no-body>
                <div class="w-50 position-relative">
                <img class="card-img-left" src="/assets/img/card-thumb-2.jpg" alt="Card cap" />
                <b-badge variant="primary" pill class="position-absolute badge-top-left">DONE</b-badge>
                </div>
                <div class="w-50">
                <b-card-body>
                    <h6 class="mb-4 card-subtitle">Wedding Cake with Flowers Macarons and Blueberries</h6>
                    <p class="card-text text-muted text-small mb-0 font-weight-light">01.06.2018</p>
                </b-card-body>
                </div>
            </b-card>
            </div>
          </swiper-slide>
          <swiper-slide> 
            <div class="pr-3 pl-3">
                <b-card class="flex-row" no-body>
                <div class="w-50 position-relative">
                <img class="card-img-left" src="/assets/img/card-thumb-3.jpg" alt="Card cap" />
                <b-badge variant="secondary" pill class="position-absolute badge-top-left">PROCESSED</b-badge>
                </div>
                <div class="w-50">
                <b-card-body>
                    <h6 class="mb-4 card-subtitle">Cheesecake with Chocolate Cookies and Cream Biscuits</h6>
                    <p class="card-text text-muted text-small mb-0 font-weight-light">27.05.2018</p>
                </b-card-body>
                </div>
            </b-card>
            </div>
          </swiper-slide>
          <swiper-slide> 
                <div class="pr-3 pl-3">
                  <b-card class="flex-row" no-body>
                  <div class="w-50 position-relative">
                    <img class="card-img-left" src="/assets/img/card-thumb-1.jpg" alt="Card cap" />
                    <b-badge variant="primary" pill class="position-absolute badge-top-left">NEW</b-badge>
                  </div>
                  <div class="w-50">
                    <b-card-body>
                        <h6 class="mb-4 card-subtitle">Cheesecake with Chocolate Cookies and Cream Biscuits</h6>
                        <p class="card-text text-muted text-small mb-0 font-weight-light">19.10.2018</p>
                    </b-card-body>
                  </div>
                </b-card>
              </div>
          </swiper-slide>
          <swiper-slide> 
            <div class="pr-3 pl-3">
                <b-card class="flex-row" no-body>
                <div class="w-50 position-relative">
                <img class="card-img-left" src="/assets/img/card-thumb-3.jpg" alt="Card cap" />
                <b-badge variant="primary" pill class="position-absolute badge-top-left">NEW</b-badge>
                </div>
                <div class="w-50">
                <b-card-body>
                    <h6 class="mb-4 card-subtitle">Cheesecake with Fresh Berries and Mint</h6>
                    <p class="card-text text-muted text-small mb-0 font-weight-light">09.04.2018</p>
                </b-card-body>
                </div>
            </b-card>
            </div>
          </swiper-slide>
        </swiper>
        <div class="text-center slider-nav">
          <button class="left-arrow btn btn-link swiper-basic-prev"><i class="simple-icon-arrow-left"></i></button>
          <div class="slider-dot-container swiper-basic-nav"></div>
          <button class="left-arrow btn btn-link swiper-basic-next"><i class="simple-icon-arrow-right"></i></button>
        </div>

        <resize-observer @notify="handleSwiperBasicResize" />
      </b-colxx>


    <b-colxx xxs="12">
    <h5 class="mb-4 card-title">{{ $t('carousel.single') }}</h5>
      </b-colxx>
      <b-colxx xxs="12" class="mb-4 pl-0 pr-0">
        <swiper :options="swiperSingleOption"  ref="swiperSingle">
          <swiper-slide> 
            <div class="pr-3 pl-3">
              <b-card class="flex-row" no-body>
                <img alt="Thumbnail" src="/assets/img/thumb-3.jpg" class="list-thumbnail responsive border-0" />
                <div class="pl-2 d-flex flex-grow-1 min-width-zero">
                  <b-card-body class="align-self-center d-flex flex-column flex-lg-row justify-content-between min-width-zero">
                    <p class="list-item-heading mb-1 truncate">Cheesecake with Chocolate Cookies and Cream Biscuits</p>
                    <p class="mb-0 text-muted text-small">Cupcakes</p>
                    <p class="mb-0 text-muted text-small">09.11.2018</p>
                    <div>
                      <b-badge variant="primary" pill class="mr-1">NEW</b-badge>
                      <b-badge variant="secondary" pill class="mr-1">ON HOLD</b-badge>
                    </div>
                  </b-card-body>
                </div>
              </b-card>
            </div>
          </swiper-slide>

           <swiper-slide> 
            <div class="pr-3 pl-3">
              <b-card class="flex-row" no-body>
                <img alt="Thumbnail" src="/assets/img/thumb-2.jpg" class="list-thumbnail responsive border-0" />
                <div class="pl-2 d-flex flex-grow-1 min-width-zero">
                  <b-card-body class="align-self-center d-flex flex-column flex-lg-row justify-content-between min-width-zero">
                    <p class="list-item-heading mb-1 truncate">Cheesecake with Fresh Berries and Mint</p>
                    <p class="mb-0 text-muted text-small">Cupcakes</p>
                    <p class="mb-0 text-muted text-small">09.04.2018</p>
                    <div>
                      <b-badge variant="primary" pill class="mr-1">NEW</b-badge>
                      <b-badge variant="secondary" pill class="mr-1">ON HOLD</b-badge>
                    </div>
                  </b-card-body>
                </div>
              </b-card>
            </div>
          </swiper-slide>
        </swiper>
        <div class="text-center slider-nav">
          <button class="left-arrow btn btn-link swiper-single-prev"><i class="simple-icon-arrow-left"></i></button>
          <div class="slider-dot-container swiper-single-nav"></div>
          <button class="left-arrow btn btn-link swiper-single-next"><i class="simple-icon-arrow-right"></i></button>
        </div>
        <resize-observer @notify="handleSwiperSingleResize" />
      </b-colxx>      
   
   
   
   
   <b-colxx xxs="12">
    <h5 class="mb-4 card-title">{{ $t('carousel.infinite') }}</h5>
      </b-colxx>
      <b-colxx xxs="12" class="mb-4 pl-0 pr-0">
        <swiper :options="swiperInfiniteOption"  ref="swiperInfinite">
          <swiper-slide> 
            <div class="pr-3 pl-3">
              <b-card  no-body>
              <div class="position-relative">
                <img class="card-img-top" src="/assets/img/card-thumb-1.jpg" alt="Card cap" />
                <b-badge variant="primary" pill class="position-absolute badge-top-left">NEW</b-badge>
                <b-badge variant="secondary" pill class="position-absolute badge-top-left-2">TRENDING</b-badge>
              </div>
                <b-card-body>
                    <h6 class="mb-4">Cheesecake with Fresh Berries and Mint</h6>
                    <p class="text-muted text-small mb-0 font-weight-light">09.04.2018</p>
                </b-card-body>
            </b-card>
          </div>
          </swiper-slide>
          <swiper-slide> 
            <div class="pr-3 pl-3">
              <b-card  no-body>
              <div class="position-relative">
                <img class="card-img-top" src="/assets/img/card-thumb-2.jpg" alt="Card cap" />
                <b-badge variant="primary" pill class="position-absolute badge-top-left">NEW</b-badge>
                <b-badge variant="secondary" pill class="position-absolute badge-top-left-2">TRENDING</b-badge>
              </div>
                <b-card-body>
                    <h6 class="mb-4">Homemade Cheesecake with Fresh Berries and Mint</h6>
                    <p class="text-muted text-small mb-0 font-weight-light">09.04.2018</p>
                </b-card-body>
            </b-card>
          </div>
          </swiper-slide>
          <swiper-slide> 
             <div class="pr-3 pl-3">
              <b-card  no-body>
              <div class="position-relative">
                <img class="card-img-top" src="/assets/img/card-thumb-3.jpg" alt="Card cap" />
                <b-badge variant="primary" pill class="position-absolute badge-top-left">NEW</b-badge>
                <b-badge variant="secondary" pill class="position-absolute badge-top-left-2">TRENDING</b-badge>
              </div>
                <b-card-body>
                    <h6 class="mb-4">Wedding Cake with Flowers Macarons and Blueberries</h6>
                    <p class="text-muted text-small mb-0 font-weight-light">09.04.2018</p>
                </b-card-body>
            </b-card>
          </div>
          </swiper-slide>
          <swiper-slide> 
               <div class="pr-3 pl-3">
              <b-card  no-body>
              <div class="position-relative">
                <img class="card-img-top" src="/assets/img/card-thumb-4.jpg" alt="Card cap" />
                <b-badge variant="primary" pill class="position-absolute badge-top-left">NEW</b-badge>
                <b-badge variant="secondary" pill class="position-absolute badge-top-left-2">TRENDING</b-badge>
              </div>
                <b-card-body>
                    <h6 class="mb-4">Cheesecake with Chocolate Cookies and Cream Biscuits</h6>
                    <p class="text-muted text-small mb-0 font-weight-light">09.04.2018</p>
                </b-card-body>
            </b-card>
          </div>
          </swiper-slide>
          <swiper-slide> 
              <div class="pr-3 pl-3">
              <b-card  no-body>
              <div class="position-relative">
                <img class="card-img-top" src="/assets/img/card-thumb-2.jpg" alt="Card cap" />
                <b-badge variant="primary" pill class="position-absolute badge-top-left">NEW</b-badge>
                <b-badge variant="secondary" pill class="position-absolute badge-top-left-2">TRENDING</b-badge>
              </div>
                <b-card-body>
                    <h6 class="mb-4">Wedding Cake with Flowers Macarons and Blueberries</h6>
                    <p class="text-muted text-small mb-0 font-weight-light">09.04.2018</p>
                </b-card-body>
            </b-card>
          </div>
          </swiper-slide>
        </swiper>
        <div class="text-center slider-nav">
          <button class="left-arrow btn btn-link swiper-infinite-prev"><i class="simple-icon-arrow-left"></i></button>
          <div class="slider-dot-container swiper-infinite-nav"></div>
          <button class="left-arrow btn btn-link swiper-infinite-next"><i class="simple-icon-arrow-right"></i></button>
        </div>
        <resize-observer @notify="handleSwiperInfiniteResize" />
      </b-colxx>      
     </b-row>
  </div>
</template>

<script>

import 'swiper/dist/css/swiper.css'
import { swiper, swiperSlide } from 'vue-awesome-swiper'
import { ResizeObserver } from 'vue-resize'
export default {
  components: {
    swiper,
    swiperSlide,
    ResizeObserver
  },
  data() {
      return {
        swiperBasicOption: {
          slidesPerView: 3,
          pagination: {
            el: '.swiper-basic-nav',
            clickable: true,
            bulletClass :'slider-dot',
            bulletActiveClass :'active',
            dynamicBullets: true,
            renderBullet(index, className) {
              return `<button class="${className}"></button>`
            }
          },
           navigation: {
            nextEl: '.swiper-basic-next',
            prevEl: '.swiper-basic-prev'
          },
          breakpoints: {
            0: {slidesPerView:1},
            1000: {slidesPerView:1},
            1400: {slidesPerView:2}
          }
        },

        swiperSingleOption: {
          slidesPerView: 1,
          pagination: {
            el: '.swiper-single-nav',
            clickable: true,
            bulletClass :'slider-dot',
            bulletActiveClass :'active',
            dynamicBullets: true,
            renderBullet(index, className) {
              return `<button class="${className}"></button>`
            }
          },
           navigation: {
            nextEl: '.swiper-single-next',
            prevEl: '.swiper-single-prev'
          }
        },
        swiperInfiniteOption: {
          slidesPerView: 4,
          loop:true,
          pagination: {
            el: '.swiper-infinite-nav',
            clickable: true,
            bulletClass :'slider-dot',
            bulletActiveClass :'active',
            dynamicBullets: true,
            renderBullet(index, className) {
              return `<button class="${className}"></button>`
            }
          },
           navigation: {
            nextEl: '.swiper-infinite-next',
            prevEl: '.swiper-infinite-prev'
          },
          breakpoints: {
            0: {slidesPerView:1},
            480: {slidesPerView:1},
            800: {slidesPerView:2},
            1200: {slidesPerView:3}
          }
        },
      }
    },
    methods: {
    handleSwiperSingleResize () {
      this.$refs.swiperSingle.update();
    },
    handleSwiperBasicResize () {
      this.$refs.swiperBasic.update();
    },
    handleSwiperInfiniteResize () {
      this.$refs.swiperInfinite.update();
    }    
  }
};
</script>
