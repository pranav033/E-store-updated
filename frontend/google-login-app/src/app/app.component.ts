import { GoogleSigninButtonModule, SocialAuthService, SocialLoginModule } from '@abacritt/angularx-social-login';
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

  constructor(private authService : SocialAuthService){
      this.authService.authState.subscribe((authData)=>{
        console.log("User logged in :");
        console.log(authData);
        console.log("ID Token --> "+authData.idToken)
      })
  }
}
