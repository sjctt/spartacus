<template>
<div>
  <b-row>
    <b-colxx xxs="12">
     
      <div class="separator mb-5"></div>
    </b-colxx>
  </b-row>
 <b-row>
    <b-colxx xxs="12">
        <b-card class="mb-4" :title="$t('forms.validation')">
          <b-form @submit.prevent="onValitadeFormSubmit">
            <b-row>

              <b-colxx sm="6">
                <b-form-group :label="$t('forms.firstname')"  >
                  <b-form-input type="text" v-model="validateForm.firstname" :state="!$v.validateForm.firstname.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('forms.firstname-message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
              <b-colxx sm="6">
                <b-form-group :label="$t('forms.lastname')">
                  <b-form-input type="text" v-model="validateForm.lastname" :state="!$v.validateForm.lastname.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('forms.lastname-message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>

              <b-colxx sm="6">
                <b-form-group :label="$t('forms.city')">
                  <b-form-input type="text" v-model="validateForm.city" :state="!$v.validateForm.city.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('forms.city-message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
              <b-colxx sm="6">
                <b-form-group :label="$t('forms.state')">
                  <b-form-select v-model="validateForm.state" :options="stateOptions" plain :state="!$v.validateForm.state.$invalid"/>
                  <b-form-invalid-feedback>{{ $t('forms.state-message')}}</b-form-invalid-feedback>
                </b-form-group>
              </b-colxx>
            </b-row>
            <b-button type="submit" variant="primary" class="mt-4" :disabled="$v.validateForm.$invalid">{{ $t('forms.submit') }}</b-button> 
          </b-form>
        </b-card>
    </b-colxx>
  </b-row>
  </div>
</template>
<script>
import InputTag from "components/Form/InputTag";
import vSelect from "vue-select";
import { validationMixin } from "vuelidate";
const { required, minLength, between } = require("vuelidate/lib/validators");

export default {
  components: {
    InputTag,
    vSelect
  },
  data() {
    return {
      name: "",
      age: 0,
      validateForm: {
        firstname: "John",
        lastname: "Doe",
        state:'',
        city: ""
      },
    };
  },
  mixins: [validationMixin],
  validations: {
    validateForm: {
      firstname: {
        required
      },
      lastname: {
        required
      },
      city: {
        required
      },
      state: {
        required
      }
    }
  },
  methods: {
   
    onValitadeFormSubmit() {
      console.log(JSON.stringify(this.validateForm));
    }
  }
};
</script>
