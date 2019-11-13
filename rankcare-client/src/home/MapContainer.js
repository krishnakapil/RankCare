import React, { Component } from 'react';
import { Map, InfoWindow, Marker, GoogleApiWrapper } from 'google-maps-react';

export class MapContainer extends Component {
    constructor(props) {
        super(props);

        this.state = {
            sitesData: props.sitesData ? props.sitesData : []
        }
    }

    render() {
        return (
            <Map google={this.props.google} style={{ width: 1000, height: 800 }} zoom={5} initialCenter={{
                lat: -24.993041,
                lng: 133.874862
            }}>
                {this.state.sitesData.map(site => {
                    return (
                        <Marker
                            key={site.id}
                            position={{ lat: site.siteLat, lng: site.siteLng }}
                            title={site.siteName}
                            data={site}
                            onClick={() => this.props.onMarkerClicked(site)}
                        />
                    )
                })}
            </Map>
        );
    }
}

export default GoogleApiWrapper({
    apiKey: (`AIzaSyDxUY1Syic2gVnXNTozIiJ3pg_V4TFsL3k`)
})(MapContainer)