import React, {useEffect} from 'react';
import {View, StyleSheet, ScrollView} from 'react-native';
import MapView, {Marker, MarkerPressEventData} from './src/MapView';
import SliderCarousel from '@juanpablocs/rn-slider-carousel';

const markers = [
  {
    lat: -12.1207698,
    lng: -77.0304079,
    title: 'KFC Miraflores',
    id: '1',
    image:
      'https://lh3.googleusercontent.com/p/AF1QipMgTanJHISBSaD6fxt7qbBUFdPyumYn_IV8_acR=s1360-w1360-h1020',
  },
  {
    lat: -12.1209514,
    lng: -77.0303299,
    title: 'Pinkberry Miraflores',
    id: '2',
    image:
      'https://lh5.googleusercontent.com/p/AF1QipPmHePOBo_poF-iS6iTjebY-liHogzgDXc6hoDi=s1354-k-no',
  },
  {
    lat: -12.1205409,
    lng: -77.030108,
    title: 'La Lucha Sangucheria',
    id: '3',
    image:
      'https://lh5.googleusercontent.com/p/AF1QipNdLgeIb8FP_xkffP5Oao9E_omitkkiOgPFFRp0=w222-h100-k-no',
  },
  {
    lat: -12.1194952,
    lng: -77.029791,
    title: 'McDonalds Miraflores',
    id: '4',
    image:
      'https://lh5.googleusercontent.com/p/AF1QipMpMsRMdgF9d28dyR_JshtAEs15Vy2OaERuML1c=w203-h152-k-no',
  },
];

const App = () => {
  const [currentMarker, setCurrentMarker] = React.useState<
    Marker | undefined
  >();
  const [currentItem, setCurrentItem] = React.useState<number>(0);

  const handleMarkerPress = (event: MarkerPressEventData) => {
    const {lat, lng, title, id} = event;
    console.log('Marker pressed:', {lat, lng, title, id});
    setTimeout(() => {
      setCurrentItem(Number(id));
    }, 200);
  };

  const handlerCurrentItem = (id: number) => {
    console.log('Current item id:', id);
    setCurrentMarker(markers.find(m => m.id === String(id)));
  };

  useEffect(() => {
    console.log('App mounted');

    return () => {
      console.log('App unmounted');
    };
  }, []);

  return (
    <View style={styles.container}>
      <MapView
        markers={markers}
        zoom={10}
        style={styles.map}
        onMarkerPress={handleMarkerPress}
        currentMarker={currentMarker}
      />
      <ScrollView
        style={styles.slide}
        contentInsetAdjustmentBehavior="automatic">
        <SliderCarousel
          currentItem={currentItem}
          onCurrentItem={handlerCurrentItem}
          entries={markers.map(m => ({
            image: m.image,
            id: Number(m.id),
            title: m.title,
          }))}
        />
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    ...StyleSheet.absoluteFillObject,
    justifyContent: 'flex-end',
    alignItems: 'center',
  },
  map: {
    ...StyleSheet.absoluteFillObject,
  },
  slide: {
    position: 'absolute',
    width: '100%',
    height: 220,
    bottom: 100,
  },
});

export default App;
