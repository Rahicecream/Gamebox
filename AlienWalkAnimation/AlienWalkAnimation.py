import os, pygame,sys;
from itertools import cycle
from pygame.locals import *

# Setting animation window at the center of the screen
os.environ['SDL_VIDEO_WINDOW_POS'] = 'center'

# Frames per second
FPS = 70
DELAY = 80

#Window length
WINDOWWIDTH = 640
WINDOWHEIGHT = 480

#Animation constants
GROUNDHEIGHT = 60

TIMELINE_START = 0
MORNING_TIMELINE_START = 0
MORNING_TIMELINE_END = 61
NOON_TIMELINE_START = 61
NOON_TIMELINE_END = 140
NIGHT_TIMELINE_START = 140
TIMELINE_END =  350

PLANE_ENTRY_POINT = 30
FRUIT_FALL_POINT = 98
SPACESHIP_ENTRY_POINT = 65
CHARACTER_DISAPPEAR_POINT = 231

SUN_POSITION = (65,20)
MOON_POSITION = (80,35)


assert GROUNDHEIGHT < WINDOWHEIGHT, 'Ground needs to lie within the window size'

ORGIN = (0,0)
PIX_MOVE_X = 0
PIX_MOVE_Y = 0


class Day_background():
    # GRADIENT FILL
    # Thanks Jaseman - 8th August 2012

    point = 0
    
    bluesky = pygame.Surface((WINDOWWIDTH,WINDOWHEIGHT))
    r=25; g=180; b=244
    for l in range (0,255):
        pygame.draw.rect(bluesky,(r,g,b),(0,l-1,WINDOWWIDTH,l))
        r=r+1;g=g+1;b=b+1
        if r>=255: r=255
        if g>=255: g=255
        if b>=255: b=255

    
    ground = pygame.Surface((WINDOWWIDTH,GROUNDHEIGHT))
    r=255; g=147; b=84
    for l in range (0,128):
        pygame.draw.rect(ground,(r,g,b),(0,l-2,WINDOWWIDTH,l))
        r=r-2;g=g-2;b=b-2
        if r<=0: r=0
        if g<=0: g=0
        if b<=0: b=0

class Noon_background():
    # GRADIENT FILL
    # Thanks Jaseman - 8th August 2012

    point = 0
    
    yellowsky = pygame.Surface((WINDOWWIDTH,WINDOWHEIGHT))
    r=139; g=47; b=26
    for l in range (0,255):
        pygame.draw.rect(yellowsky,(r,g,b),(0,l-1,WINDOWWIDTH,l))
        r=r+1;g=g+1;b=b+1
        if r>=255: r=255
        if g>=204: g=204
        if b>=1: b=1

    
    ground = pygame.Surface((WINDOWWIDTH,GROUNDHEIGHT))
    r=255; g=147; b=84
    for l in range (0,128):
        pygame.draw.rect(ground,(r,g,b),(0,l-2,WINDOWWIDTH,l))
        r=r-2;g=g-2;b=b-2
        if r<=0: r=0
        if g<=0: g=0
        if b<=0: b=0

class Night_background():
    # GRADIENT FILL
    # Thanks Jaseman - 8th August 2012

    point = 0
    
    darksky = pygame.Surface((WINDOWWIDTH,WINDOWHEIGHT))
    r=239; g=100; b=24
    for l in range (0,255):
        pygame.draw.rect(darksky,(r,g,b),(0,l-1,WINDOWWIDTH,l))
        r=r+1;
        g=g+1;b=b+1
        if r>=0: r=0
        if g>=1: g=1
        if b>=93: b=93

    
    ground = pygame.Surface((WINDOWWIDTH,GROUNDHEIGHT))
    r=0; g=46; b=122
    for l in range (0,128):
        pygame.draw.rect(ground,(r,g,b),(0,l-2,WINDOWWIDTH,l))
        r=r-2;g=g-2;b=b-2
        if r<=0: r=0
        if g<=0: g=0
        if b<=0: b=0


class Clouds():

    def __init__(self):
        self.cloud1 = pygame.image.load('AlienWalkAnimationPictures/clouds1.png')
        self.cloud2 = pygame.image.load('AlienWalkAnimationPictures/clouds2.png')
        self.starlayer = pygame.image.load('AlienWalkAnimationPictures/starlayer.png')
        
        # cloud's inital position
        self.x1 = 60
        self.y1 = 60
        self.x2 = 300
        self.y2 = 120
        self.x3 = -10
        self.y3 = 60

    def handle_keys(self):
        """ Handles Keys """
        key = pygame.key.get_pressed()
        dist = 1 # distance moved in 1 frame, try changing it to 5

        if key[pygame.K_LEFT]: # Left key - Show next picture in the array
            self.x1 += dist
            self.x2 += dist

        if key[pygame.K_RIGHT]: # Right key - Show next picture in the array
            self.x1 -= dist
            self.x2 -= dist
            
    def showstarlayer(self,surface):
        surface.blit(self.starlayer, (self.x3, self.y3))
        
    def changenoonclouds(self):
        self.cloud2 = self.cloud2 = pygame.image.load('AlienWalkAnimationPictures/noonclouds.png')

    def changemorningclouds(self):
        self.cloud2 = self.cloud2 = pygame.image.load('AlienWalkAnimationPictures/clouds2.png')

    def changenightclouds(self):
        self.cloud2 = self.cloud2 = pygame.image.load('AlienWalkAnimationPictures/cloudsnight.png')
           
    def drawclouds(self,surface):
        surface.blit(self.cloud2, (self.x1, self.y1))
        surface.blit(self.cloud2, (self.x2, self.y2))

class Plane():

    def __init__(self):
        self.plane = pygame.image.load('AlienWalkAnimationPictures/plane.png')
        
        
        self.x = 700
        self.y = 120

    def handle_keys(self):
        """ Handles Keys """
        key = pygame.key.get_pressed()
        dist = 25 

        if key[pygame.K_LEFT]: 
            self.x -= dist

        if key[pygame.K_RIGHT]: 
            self.x += dist
            

           
    def drawplane(self,surface):
        surface.blit(self.plane, (self.x, self.y))

class SpaceShip():

    def __init__(self):
        self.spaceship = pygame.image.load('AlienWalkAnimationPictures/spaceship.png')
        self.tempspaceship = self.spaceship
       
        self.x = -100
        self.y = 300
        self.nw = 0
        self.nh = 0
        self.ow = -1
        self.oh = -1

    def handle_keys(self):
        """ Handles Keys """
        key = pygame.key.get_pressed()
        dist = 0.5 

        if key[pygame.K_LEFT]: 
            self.x += dist

        if key[pygame.K_RIGHT]: 
            self.x -= dist
            
    def go_up(self):
        """ Handles Keys """
        key = pygame.key.get_pressed()
        dist = 2 
                
        if key[pygame.K_LEFT]: 
            self.y -= dist

        if key[pygame.K_RIGHT]: 
            self.y += dist

    def resize(self):
        key = pygame.key.get_pressed()
        

        if key[pygame.K_LEFT]: 
            self.spaceship = self.tempspaceship
            if self.ow < 0 and self.oh < 0:
                w,h = self.spaceship.get_size()
                print(w,h)
                
                self.nw = w*(5/100)
                self.nh = h*(5/100)
                self.x +=2
                self.spaceship = pygame.transform.smoothscale(self.spaceship, (int(w-self.nw), int(h-self.nh)))
                self.ow = w - self.nw
                self.oh = h - self.nh
                print(self.ow,self.oh)
            else:
                self.spaceship = self.tempspaceship
                self.nw = self.ow*(5/100)
                self.nh = self.oh*(5/100)
                self.x +=2
                self.spaceship = pygame.transform.smoothscale(self.spaceship, (int(self.ow-self.nw), int(self.oh-self.nh)))
                self.ow = self.ow - self.nw
                self.oh = self.oh - self.nh
                print(self.ow,self.oh)

        if key[pygame.K_RIGHT]: 
            self.spaceship = self.tempspaceship
            if self.ow < 0 and self.oh < 0:
                w,h = self.spaceship.get_size()
                print(w,h)
                
                self.nw = w*(5/100)
                self.nh = h*(5/100)
                self.x -=2
                self.spaceship = pygame.transform.smoothscale(self.spaceship, (int(w+self.nw), int(h+self.nh)))
                self.ow = w + self.nw
                self.oh = h + self.nh
                print(self.ow,self.oh)
            else:
                
                self.nw = self.ow*(5/100)
                self.nh = self.oh*(5/100)
                self.x -=2
                self.spaceship = pygame.transform.smoothscale(self.spaceship, (int(self.ow+self.nw), int(self.oh+self.nh)))
                self.ow = self.ow + self.nw
                self.oh = self.oh + self.nh
                print(self.ow,self.oh)
        
        
    def drawspaceship(self,surface):
        surface.blit(self.spaceship, (self.x, self.y))

class Fruit():

    def __init__(self):
        self.fruit = pygame.image.load('AlienWalkAnimationPictures/apple.png')
        
        # fruit's inital position
        self.x = 110
        self.y = 330

    def handle_keys(self):
        """ Handles Keys """
        key = pygame.key.get_pressed()
        dist = 5 # distance moved in 1 frame, try changing it to 5

        if key[pygame.K_LEFT]: # Left key - Show next picture in the array
            if self.y < (WINDOWHEIGHT- GROUNDHEIGHT):
                self.y += dist

            self.x += dist

        if key[pygame.K_RIGHT]: # Right key - Show next picture in the array
            if self.y >= 330:
                self.y -= dist

            self.x -= dist
            
    def drawfruit(self,surface):
        surface.blit(self.fruit, (self.x, self.y))

class Tree():

    def __init__(self):
        self.tree = pygame.image.load('AlienWalkAnimationPictures/tree.png')
        
       
        self.x = 20
        self.y = 275

    def handle_keys(self):
        """ Handles Keys """
        key = pygame.key.get_pressed()
        dist = 5 

        if key[pygame.K_LEFT]: 
            self.x += dist

        if key[pygame.K_RIGHT]: 
            self.x -= dist
       
    def drawtree(self,surface):
        surface.blit(self.tree, (self.x, self.y))

class Building():

    def __init__(self):
        self.building = pygame.image.load('AlienWalkAnimationPictures/backgroundbuilding2.png')
        
        
        self.x = 120
        self.y = 311

    def handle_keys(self):
        """ Handles Keys """
        key = pygame.key.get_pressed()
        dist = 0.3 

        if key[pygame.K_LEFT]: 
            self.x += dist

        if key[pygame.K_RIGHT]: 
            self.x -= dist

           
    def drawbuilding(self,surface):
        surface.blit(self.building, (self.x, self.y))

class Moon():

    def __init__(self):
        self.moon = pygame.image.load('AlienWalkAnimationPictures/moon_small.png')
        
    def drawmoon(self,surface):
        surface.blit(self.moon, MOON_POSITION)
        
class Sun():

    def __init__(self):
        self.sun = pygame.image.load('AlienWalkAnimationPictures/sun.png')
 
    def drawsun(self,surface):
        surface.blit(self.sun, SUN_POSITION)

class Character():
  
    def __init__(self):
        """ The constructor of the class """
        self.a = pygame.image.load('AlienWalkAnimationPictures/1.png')
        self.b = pygame.image.load('AlienWalkAnimationPictures/2.png')
        self.c = pygame.image.load('AlienWalkAnimationPictures/3.png')
        self.d = pygame.image.load('AlienWalkAnimationPictures/4.png')
        self.currentpicture = self.a
        self.tracker = 0
        self.temp = 0
        self.picturearray = [self.a,self.b,self.c,self.d]
        self.picturecycle = cycle(self.picturearray)

        # the character's initial position
        self.x = 590
        self.y = 313

    def handle_keys(self):
        """ Handles Keys """
        key = pygame.key.get_pressed()
        dist = 5 

        if key[pygame.K_LEFT]: 

           if self.tracker <= TIMELINE_END :
               if self.temp > 3:
                   self.temp = 0
               print(self.tracker)
               self.currentpicture = self.picturearray[self.temp]
               self.temp += 1
               self.tracker += 1
              

        if key[pygame.K_RIGHT]: 
            if self.temp == 4 or self.temp < 0:
               self.temp = 3

            if not (self.tracker < 0):
                print(self.tracker)
                self.currentpicture = self.picturearray[self.temp]
                self.temp -= 1
                self.tracker -= 1
                
    def move_handle_keys(self):
        """ Handles Keys """
        key = pygame.key.get_pressed()
        dist = 5 

        if key[pygame.K_LEFT]: 
            self.x -=dist
            
        if key[pygame.K_RIGHT]: 
            self.x +=dist
        
    def drawcharacter(self, surface):
        """ Draw on surface """
        # blit yourself at your current position
        surface.blit(self.currentpicture, (self.x, self.y))

class Cycle():
  
    def __init__(self):
        """ The constructor of the class """
        self.a = pygame.image.load('AlienWalkAnimationPictures/cycle1.png')
        self.b = pygame.image.load('AlienWalkAnimationPictures/cycle2.png')
        self.c = pygame.image.load('AlienWalkAnimationPictures/cycle3.png')
        self.d = pygame.image.load('AlienWalkAnimationPictures/cycle4.png')
        self.e = pygame.image.load('AlienWalkAnimationPictures/cycle5.png')
        self.f = pygame.image.load('AlienWalkAnimationPictures/cycle6.png')
        self.g = pygame.image.load('AlienWalkAnimationPictures/cycle7.png')
        self.h = pygame.image.load('AlienWalkAnimationPictures/cycle8.png')
        self.currentpicture = self.a
        self.tracker = 0
        self.temp = 0
        self.picturearray = [self.a,self.b,self.c,self.d,self.e,self.f,self.g,self.h]
        self.picturecycle = cycle(self.picturearray)

        # the cycle's position
        self.x = 0
        self.y = 365

    def handle_keys(self):
        """ Handles Keys """
        key = pygame.key.get_pressed()
        dist = 8 

        if key[pygame.K_LEFT]: 

           if self.temp > 7:
               self.temp = 0
           self.currentpicture = self.picturearray[self.temp]
           self.temp += 1
           self.tracker += 1
           self.x +=dist

        if key[pygame.K_RIGHT]: 
            if self.temp == 8 or self.temp < 0:
               self.temp = 7
               
            self.currentpicture = self.picturearray[self.temp]
            self.temp -= 1
            self.tracker -= 1
            self.x -=dist
        
    def drawcycle(self, surface):
        """ Draw on surface """
        # blit yourself at your current position
        surface.blit(self.currentpicture, (self.x, self.y))


def main():


    pygame.init()
    FPSCLOCK = pygame.time.Clock()
    workboard = pygame.display.set_mode((WINDOWWIDTH,WINDOWHEIGHT))
    pygame.display.set_caption('Alien Walking Animation')

    
    daybackground = Day_background() # create a day background instance
    nightbackground = Night_background()
    noonbackground = Noon_background()
    character = Character()
    
    clouds = Clouds()
    plane = Plane()
    sun = Sun()
    tree = Tree()
    fruit = Fruit()
    cycle = Cycle()
    building = Building()
    moon = Moon()
    spaceship = SpaceShip()
            
    while True:


        # Capture window close and quit
        for event in pygame.event.get():
            if event.type == QUIT:
                pygame.quit()
                sys.exit()

        
        if character.tracker >= TIMELINE_START and character.tracker >= MORNING_TIMELINE_START and character.tracker <= MORNING_TIMELINE_END:
            workboard.blit(daybackground.bluesky,ORGIN)
            workboard.blit(daybackground.ground,(0,(WINDOWHEIGHT-GROUNDHEIGHT)))
            clouds.changemorningclouds()
            

        if character.tracker >= TIMELINE_START and character.tracker > NOON_TIMELINE_START and character.tracker <= NOON_TIMELINE_END:
            workboard.blit(noonbackground.yellowsky,ORGIN)
            if character.tracker >= SPACESHIP_ENTRY_POINT:
                    spaceship.drawspaceship(workboard)
                    spaceship.handle_keys()
            workboard.blit(noonbackground.ground,(0,(WINDOWHEIGHT-GROUNDHEIGHT)))
            clouds.changenoonclouds()
            

        if character.tracker >= TIMELINE_START and character.tracker > NIGHT_TIMELINE_START and character.tracker <= TIMELINE_END:
            workboard.blit(nightbackground.darksky,ORGIN)
            if character.tracker >= SPACESHIP_ENTRY_POINT and character.tracker <= CHARACTER_DISAPPEAR_POINT:
                spaceship.handle_keys()
            if character.tracker >= SPACESHIP_ENTRY_POINT and character.tracker > CHARACTER_DISAPPEAR_POINT:
                spaceship.go_up()
            
            spaceship.drawspaceship(workboard)
            workboard.blit(nightbackground.ground,(0,(WINDOWHEIGHT-GROUNDHEIGHT)))

        if character.tracker >= SPACESHIP_ENTRY_POINT and character.tracker > CHARACTER_DISAPPEAR_POINT and character.tracker%3 == 0 and character.tracker <= TIMELINE_END:
            spaceship.resize()   
    
        if character.tracker >= TIMELINE_START and character.tracker <= NOON_TIMELINE_END:
            sun.drawsun(workboard)
            clouds.handle_keys()
            clouds.drawclouds(workboard)
            building.drawbuilding(workboard)
            building.handle_keys()
            tree.drawtree(workboard)
            tree.handle_keys()
            
            if character.tracker >=PLANE_ENTRY_POINT and character.tracker >= TIMELINE_START:
                plane.handle_keys()
                plane.drawplane(workboard)
        
            if character.tracker <=FRUIT_FALL_POINT and character.tracker >= TIMELINE_START:
                fruit.drawfruit(workboard)
                fruit.handle_keys()

            if character.tracker >= TIMELINE_START and character.tracker >=NOON_TIMELINE_START and character.tracker <= NOON_TIMELINE_END:
                cycle.drawcycle(workboard)
                cycle.handle_keys()
       
        if character.tracker >= TIMELINE_START and character.tracker > NIGHT_TIMELINE_START and character.tracker <= TIMELINE_END:
            clouds.showstarlayer(workboard)
            clouds.drawclouds(workboard)
            clouds.changenightclouds()
            moon.drawmoon(workboard)
            building.drawbuilding(workboard)
            

        if character.tracker >= TIMELINE_START and character.tracker > NIGHT_TIMELINE_START and character.tracker <= CHARACTER_DISAPPEAR_POINT:
            clouds.handle_keys()
            building.handle_keys()
            character.move_handle_keys()
            spaceship.handle_keys()

        
        if character.tracker >= TIMELINE_START and character.tracker <= CHARACTER_DISAPPEAR_POINT:
            character.drawcharacter(workboard)

        character.handle_keys()
            
        # Redraw the screen and wait a clock tick.
        pygame.time.delay(DELAY)
        pygame.display.update()
        FPSCLOCK.tick(FPS)

if __name__ == '__main__':
    main()
