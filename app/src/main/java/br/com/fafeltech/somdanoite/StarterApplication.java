/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package br.com.fafeltech.somdanoite;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;

import br.com.fafeltech.somdanoite.model.Evento;
import br.com.fafeltech.somdanoite.model.EventoFavorito;


public class StarterApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    MobileAds.initialize(this, getString(R.string.admob_app_id));

    // Habilite armazenamento local.
    Parse.enableLocalDatastore(this);

    // Codigo de configuração do App
    Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
            .applicationId(getString(R.string.parse_app_id))
            .clientKey(getString(R.string.parse_client_key))
            .server("https://parseapi.back4app.com/")
    .build()
    );

    ParseObject.registerSubclass(Evento.class);
    ParseObject.registerSubclass(EventoFavorito.class);

    ParseACL defaultACL = new ParseACL();
    // Optionally enable public read access.
    defaultACL.setPublicReadAccess(true);
  }
  /*
  * teste de update
  *
  * */
}
