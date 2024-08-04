import React, {useEffect} from 'react';
import {
  requireNativeComponent,
  ViewProps,
  DeviceEventEmitter,
} from 'react-native';

export type Marker = {
  lat: number;
  lng: number;
  title?: string;
  id?: string;
  image?: string;
};

export type MarkerPressEventData = {
  lat: number;
  lng: number;
  title?: string;
  id?: string;
  image?: string;
};

export interface MapViewProps extends ViewProps {
  zoom: number;
  markers: Marker[];
  currentMarker?: Marker;
  onMarkerPress?: (event: MarkerPressEventData) => void;
}

const MapViewNativeComponent = requireNativeComponent<MapViewProps>('MapView');

const MapView: React.FC<MapViewProps> = ({
  zoom,
  markers,
  onMarkerPress,
  currentMarker,
  ...props
}) => {
  useEffect(() => {
    const subscription = DeviceEventEmitter.addListener(
      'onMarkerPress',
      (event: MarkerPressEventData) => {
        if (onMarkerPress) {
          onMarkerPress(event);
        }
      },
    );
    return () => {
      subscription.remove();
    };
  }, [onMarkerPress]);

  return (
    <MapViewNativeComponent
      {...props}
      markers={markers}
      onMarkerPress={onMarkerPress}
      currentMarker={currentMarker}
      zoom={zoom}
    />
  );
};

export default MapView;
