import { GoogleSigninButtonModule, SocialAuthService, SocialLoginModule } from '@abacritt/angularx-social-login';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SocialLoginModule, GoogleSigninButtonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'google-login-app';
  user : any;
  googleLoginUrl = "http://localhost:9090/auth/login-with-google";

  constructor(private authService : SocialAuthService, private http : HttpClient){
      this.authService.authState.subscribe((authData)=>{
        console.log("User logged in :");
        console.log(authData);
        console.log("ID Token --> "+authData.idToken);

        if(authData)
        {
          this.http.post(this.googleLoginUrl,{idToken : authData.idToken}).subscribe((res)=>{
              console.log("Response from backend");
              console.log(res);
              this.user = res;
          })
        }
      })
  }
}
