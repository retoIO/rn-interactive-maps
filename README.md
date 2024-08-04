This is a new [**React Native**](https://reactnative.dev) project, bootstrapped using [`@react-native-community/cli`](https://github.com/react-native-community/cli).

# Getting Started

>npm install @juanpablocs/rn-interactive-maps

## Android

Add your API key to your manifest file (android/app/src/main/AndroidManifest.xml):

```xml
<application>
   <!-- You will only need to add this meta-data tag, but make sure it's a child of application -->
   <meta-data
     android:name="com.google.android.geo.API_KEY"
     android:value="Your Google maps API Key Here"/>
</application>
```

## Implement Component
MapView component
```jsx
import MapView from '@juanpablocs/rn-interactive-maps';

const App = () => {
  return (
    <MapView
      markers={[]}
      zoom={10}
      style={styles.map}
      onMarkerPress={console.log}
    />
  )
}
const styles = StyleSheet.create({
  map: {
    ...StyleSheet.absoluteFillObject,
  },
})
```

## Props 💅
|               Prop               | Description                                                                     |                                                                                              Type                                                                                               | Default | Required |
| :------------------------------: | :------------------------------------------------------------------------------ | :---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: | :-----: | :------: |
|         markers         | List  of coords                                              |                                                            ```Marker[];```                                                           |    -    |  true   |
|         zoom         | Level zoom                                             |                                                            ```number```                                                           |    -    |  true   |
|         onMarkerPress         | Tap click                                             |                                                            ```(event: MarkerPressEventData) => void```                                                           |    -    |  false   |
|         currentMarker         | Center current marker                                             |                                                            ```Marker```                                                           |    -    |  false   |

### Typing of Marker
```ts
type Marker = {
  lat: number;
  lng: number;
  title?: string;
  id?: string;
  image?: string;
};
// usage
import { Marker } from '@juanpablocs/rn-interactive-maps';
```

## Start your Application

Let Metro Bundler run in its _own_ terminal. Open a _new_ terminal from the _root_ of your React Native project. Run the following command to start your _Android_ or _iOS_ app:

### For Android

```bash
# using npm
npm run android

# OR using Yarn
yarn android
```

## Congratulations! :tada:

You've successfully run and modified your React Native App. :partying_face:

