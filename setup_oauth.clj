(config
 (text-field
  :name "client_id"
  :label "Client ID"
  :placeholder "Enter your OAuth Client ID"
  :required true)
 (password-field
  :name "client_secret"
  :label "Client Secret"
  :placeholder "Enter your OAuth Client Secret"
  :required true)

 (oauth2/refresh-token-with-client-credentials
  (token
   (source
    (http/post
     :base-url "https://app.7shifts.com"
     :url "/oauth2/token"
     (body-params
      "grant_type" "client_credentials"
      "client_id" "{client_id}"
      "client_secret" "{client_secret}"
      "scope" "v1_access")
     (body-param-format "application/x-www-form-urlencoded")))
   (fields
    access_token :<= "access_token"
    token_type :<= "token_type"
    realm_id :<= "realmId"
    expires_in :<= "expires_in")))




 (default-source

  (http/get
   :base-url "https://api.7shifts.com/v2"
   (header-params
    "Accept" "application/json"))


  (auth/oauth2)


  (paging/cursor
   :cursor-query-param "cursor"
   :cursor-value-path "meta#cursor#next"
   :limit-query-param "limit"
   :limit-value 200)


  (error-handler
   (when :status 401 :action refresh)
   (when :status 429 :action rate-limit)))

 (entity companies
         (api-docs-url "https://developers.7shifts.com/reference/listcompanies")
         (source
          (http/get :url "/companies")
          (extract-path "data")
          (setup-test
           (upon-receiving :code 200 :action (pass))))
         (fields
          id            :id
          name
          email
          mobile_number
          status
          created       :<= "created"
          modified      :<= "modified"
          id
          uuid
          country
          photo
          plan_id
          expires
          days_to_expire
          converted
          pos
          start_week_on))


 (entity users
         (api-docs-url "https://developers.7shifts.com/reference/listuserslist")
         (source
          (http/get :url "/company/{company_id}/users")
          (extract-path "data")
          (setup-test
           (upon-receiving :code 200 :action (pass))))
         (fields
          id            :id
          first_name
          last_name
          email
          mobile_number
          status
          identity_id
          company_id
          preferred_first_name
          preferred_last_name
          pronouns
          home_number
          address
          postal_zip
          city
          prov_state
          invite_status
          last_login
          active
          photo
          notes
          hire_date
          timezone
          type
          punch_id
          employee_id
          max_weekly_hours
          invited
          invite_accepted
          is_new
          birth_date
          language
          appear_as_employee
          subscribe_to_updates
          skill_level
          hourly_wage
          wage_type
          sms_me_schedules
          notify_ot_risk
          push))

 (entity locations
         (api-docs-url "https://developers.7shifts.com/reference/getlocationlistbycompany")
         (source
          (http/get :url "/company/{company_id}/locations")
          (extract-path "data"))
         (fields
          id            :id
          name
          city
          country
          timezone
          hash          :<= "hash"
          company_id
          state
          formatted_address
          lat
          lng
          place_id
          timezone_updated
          mapping_id
          department_based_budget
          holiday_pay
          auto_send_log_book_time
          mon_hours_close
          tue_hours_close
          wed_hours_close
          thu_hours_close
          fri_hours_close
          sat_hours_close
          sun_hours_close
          mon_hours_open
          tue_hours_open
          wed_hours_open
          thu_hours_open
          fri_hours_open
          sat_hours_open
          sun_hours_open
          mon_is_closed
          tue_is_closed
          wed_is_closed
          thu_is_closed
          fri_is_closed
          sat_is_closed
          sun_is_closed
          shift_feedback
          message
          created
          modified))

 (entity shifts
         (api-docs-url "https://developers.7shifts.com/reference/listshift")
         (source
          (http/get :url "/company/{company_id}/shifts")
          (extract-path "data"))
         (fields
          id            :id
          user_id
          location_id
          department_id
          role_id
          start         :<= "start"
          end           :<= "end"
          modified      :<= "modified"
          company_id
          station
          station_name
          station_id
          close
          business_decline
          hourly_wage
          notes
          draft
          notified
          open
          unassigned
          unassigned_skill_level
          open_offer_type
          publish_status
          attendance_status
          late_minutes
          created
          soft_deleted
          deleted
          breaks))

 (entity time_punches
         (api-docs-url "https://developers.7shifts.com/reference/gettimepunches")
         (source
          (http/get :url "/company/{company_id}/time_punches")
          (extract-path "data"))
         (fields
          id            :id
          user_id
          location_id
          clocked_in    :<= "clocked_in"
          clocked_out   :<= "clocked_out"
          notes
          modified      :<= "modified"
          company_id
          shift_id
          editable_punch
          role_id
          department_id
          hourly_wage
          approved
          auto_clocked_out
          clocked_in_offline
          clocked_out_offline
          tips
          created
          deleted
          pos_type
          breaks))