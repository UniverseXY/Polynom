package ru.amaks.ui

import ru.amaks.math.polynom.Newton
import ru.amaks.math.polynom.Newton_2
import ru.amaks.math.polynom.Polynom
import ru.amaks.ui.painting.CartesianPainter
import ru.amaks.ui.painting.FunctionPainter
import ru.amaks.ui.painting.Painter
import ru.amaks.ui.painting.Plane
import ru.amaks.ui.painting.PointsPainter
import java.awt.*
import java.awt.event.*
import javax.swing.*
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener


class PolynomUI: JFrame() {
  /*  inner class xMinListener : ChangeListener {
        override fun stateChanged(e : ChangeEvent?) {
            ModelMax.minimum = (ModelMin.value as Double + 0.1)

    }*/
    inner class CtrlType ( val POINTS : JPanel, val POLYNOM : JPanel, val DERIVATIVE : JPanel) : JPanel() {
    }

    val minDim = Dimension(600, 600)
    val panel: GraphicsPanel
    val ctrlPanel: JPanel
    val label_1: JLabel
    val label_2: JLabel
    val label_3: JLabel
    val label_4: JLabel
    var xMin: JSpinner
    var yMin : JSpinner
    var xMax: JSpinner
    var yMax : JSpinner
    val showPoints : JCheckBox
    val showPolynom : JCheckBox
    val showDerivative : JCheckBox
    val pointPanel : JPanel
    val polPanel : JPanel
    val derivPanel : JPanel
    val xMaxM : SpinnerNumberModel
    val xMinM : SpinnerNumberModel
    val yMaxM : SpinnerNumberModel
    val yMinM : SpinnerNumberModel
    var painters : MutableList<Painter> = mutableListOf<Painter>()
    var isShowPoints : Boolean = true
    var isShowPolynom : Boolean = false
    var isShowDerivative : Boolean = false
    init {
        defaultCloseOperation = EXIT_ON_CLOSE // Чтобы приложение завершало работу при закрытии
        minimumSize = minDim // Устанавливаем минимальный размер формы
        xMinM = SpinnerNumberModel(-5.0, -100.0, 4.9, 0.1)
        yMinM = SpinnerNumberModel(-5.0, -100.0, 4.9, 0.1)
        xMaxM = SpinnerNumberModel(5.0, -4.9, 100.0, 0.1)
        yMaxM = SpinnerNumberModel(5.0, -4.9, 100.0, 0.1)
        xMin = JSpinner(xMinM)
        yMin = JSpinner(yMinM)
        xMax = JSpinner(xMaxM)
        yMax = JSpinner(yMaxM)
        val mainPlane = Plane(xMinM.value as Double, xMaxM.value as Double, yMinM.value as Double, yMaxM.value as Double)
        val cartesianPainter = CartesianPainter(mainPlane)
        val pointsPainter = PointsPainter(mainPlane)
        var polynom = Newton_2(mutableMapOf())
        val polPainter = FunctionPainter(mainPlane, polynom::invoke)
        val derivPainter = FunctionPainter(mainPlane){
            polynom.find_Derivative().invoke(it)
        }
        painters.add(cartesianPainter)
        painters.add(pointsPainter)
        title = "Построение графика функции"
        /* Лучше создать конструктор, принимающий список у GraphicsPanel
        panel = GraphicsPanel(*painters.toTypedArray()).apply {
            background = Color.white
        }*/
        panel = GraphicsPanel(painters).apply {
            background = Color.white
        }
        panel.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked (e : MouseEvent?) {
     
                e?.apply {
                    if (button ==  MouseEvent.BUTTON1) {
                        val isAdded = pointsPainter.addPoint(mainPlane.xScr2Crt(x), mainPlane.yScr2Crt(y))
                        if (isAdded) polynom.addPoint(mainPlane.xScr2Crt(x), mainPlane.yScr2Crt(y))
                    }
                    else if (button == MouseEvent.BUTTON3) {
                        val isDeleted = pointsPainter.deletePoint(mainPlane.xScr2Crt(x),mainPlane.yScr2Crt(y) )
                        // Если точка удалена с экрана, то удаляем ее из узлов в полиноме
                        if (isDeleted) {
                            polynom.deletePointX(pointsPainter.deletedPointX)
                        }
                    }
                    panel.repaint()
                }

            }
        })
        //val color = JColorChooser.showDialog(null, "Выберите цвет", color.background)
        // При щелчках для точек с одинаковыми запрещать клик, смотрим на некоторую окрестность
        mainPlane.pixelSize = panel.size
        // Обработчик события изменения размеров панели
        panel.addComponentListener(object: ComponentAdapter(){
            override fun componentResized(e : ComponentEvent?){
                mainPlane.pixelSize = panel.size
                panel.repaint() // Вызов метода paint с buffered graphics
            }
        })
        ctrlPanel = JPanel().apply {
            border = BorderFactory.createLineBorder(Color.BLACK)
        }
        pointPanel = JPanel().apply {
            background = Color.GREEN
            pointsPainter.pColor = background
        }
        polPanel = JPanel().apply {
            background = Color.blue
            polPainter.funColor = background
        }
        derivPanel = JPanel().apply {
            background = Color.pink
            derivPainter.funColor = background
        }
        pointPanel.addMouseListener( object : MouseAdapter () {
            override fun mouseClicked(e: MouseEvent?) {
                e?.let {
                    val colorPanel = it.source as JPanel
                    val color = JColorChooser.showDialog(null, "Выберите цвет", colorPanel.background)
                    colorPanel.background = color
                    pointsPainter.pColor = color
                    panel.repaint()
                }
            }
        })
        polPanel.addMouseListener( object : MouseAdapter () {
            override fun mouseClicked(e: MouseEvent?) {
                e?.let {
                    val colorPanel = it.source as JPanel
                    val color = JColorChooser.showDialog(null, "Выберите цвет", colorPanel.background)
                    colorPanel.background = color
                    polPainter.funColor = color
                    panel.repaint()
                }
            }
        })
        derivPanel.addMouseListener( object : MouseAdapter () {
            override fun mouseClicked(e: MouseEvent?) {
                e?.let {
                    val colorPanel = it.source as JPanel
                    val color = JColorChooser.showDialog(null, "Выберите цвет", colorPanel.background)
                    colorPanel.background = color
                    derivPainter.funColor = color
                    panel.repaint()
                }
            }
        })
        val ctrlType = CtrlType(pointPanel, polPanel, derivPanel)
        label_1 = JLabel().apply {
            text = "Xmin:"
        }
        label_2 = JLabel().apply {
            text = "Ymin:"
        }
        label_3 = JLabel().apply {
            text = "Xmax:"
        }
        label_4 = JLabel().apply {
            text = "Ymax:"
        }

        showPoints = JCheckBox().apply {
            text = "Отображать точки"
            setSelected(true)
        }
        showPolynom = JCheckBox().apply {
            text = "Отображать график функции"
            setSelected(false)
        }
        showDerivative = JCheckBox().apply {
            text = "Отображать производную"
            setSelected(false)
        }
        showPoints.addMouseListener(object : MouseAdapter () {
            override fun mousePressed (e: MouseEvent?) {
                e?.let {
                    isShowPoints = !isShowPoints
                    if (isShowPoints) {
                        if (painters.contains(polPainter)) painters.add(pointsPainter)
                        else {
                            painters.add(painters.size, pointsPainter)
                        }
                    }
                    else painters.remove(pointsPainter)
                    panel.repaint()
                }
            }
        })
        showPolynom.addMouseListener(object : MouseAdapter () {
            override fun mouseClicked(e: MouseEvent?) {
                e?.let {
                    isShowPolynom = !isShowPolynom
                    if (!polynom._points.isEmpty() || isShowPoints) {
                        if (isShowPolynom) {
                            if (painters.contains(pointsPainter)) {
                                val pointsIndex = painters.indexOf(pointsPainter)
                                painters.add(pointsPainter)
                                painters[pointsIndex] = polPainter
                            } else
                                painters.add(polPainter)
                        } else
                            painters.remove(polPainter)
                    }
                    panel.repaint()
                }
            }
        })
        showDerivative.addMouseListener(object : MouseAdapter () {
            override fun mousePressed (e: MouseEvent?) {
                e?.let {
                    isShowDerivative= !isShowDerivative
                    if (isShowDerivative)
                        painters.add( derivPainter)
                    else painters.remove(derivPainter)
                    panel.repaint()
                }
            }
        })

        // layout = GroupLayout(contentPane).apply
        /// Добавлением компонентов занимается менеджер раскладок
        // Default - растягивает, Preferred - стягивает
        layout = GroupLayout(contentPane).apply {
            setHorizontalGroup(
                createSequentialGroup()
                    .addGap(4)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                            .addComponent(ctrlPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    )
                    .addGap(4)
            )
            setVerticalGroup(
                createSequentialGroup()
                    .addGap(4)
                    .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addGap(4)
                    .addComponent(ctrlPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(4)
            )

        }

        /*val xMinListener = xMinListener()
      xMin.addChangeListener(xMinListener) 1-й способ*/
        /* xMin.addChangeListener(object : ChangeListener {
             override fun stateChanged(e : ChangeEvent?) {
                 ModelMax.minimum = (ModelMin.value as Double + 0.1)
             }
         }) 2-й способ передачи объекта типа changeListener*/
        xMin.addChangeListener{
            xMaxM.minimum = xMin.value as Double + 0.1
            mainPlane.xSegment = Pair(xMin.value as Double, xMax.value as Double)
            panel.repaint()
        }
        xMax.addChangeListener{
            xMinM.maximum = xMax.value as Double - 0.1
            mainPlane.xSegment = Pair(xMin.value as Double, xMax.value as Double)
            panel.repaint()
        }
        yMin.addChangeListener{
            yMaxM.minimum = yMin.value as Double + 0.1
            mainPlane.ySegment = Pair(yMin.value as Double, yMax.value as Double)
            panel.repaint()
        }
        yMax.addChangeListener{
            yMinM.maximum = yMax.value as Double - 0.1
            mainPlane.ySegment = Pair(yMin.value as Double, yMax.value as Double)
            panel.repaint()
        }

      /*  ctrlPanel.layout = GroupLayout(ctrlPanel).apply {
                setHorizontalGroup(
                    createSequentialGroup()
                        .addGap(4)
                        .addGroup(
                            createParallelGroup()
                                .addComponent(label_1,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label_2,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(4)
                        .addGroup(
                            createParallelGroup()
                                .addComponent(xMin, 100,  100,  GroupLayout.PREFERRED_SIZE)
                                .addComponent(yMin, 100,  100,  GroupLayout.PREFERRED_SIZE)

                        )
                        .addGap(30)
                        .addGroup(
                            createParallelGroup()
                                .addComponent(label_3,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label_4,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(8)
                        .addGroup(
                            createParallelGroup()
                                .addComponent(xMax, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                                .addComponent(yMax, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(40)
                        .addGroup(
                            createParallelGroup()
                                .addComponent(check_1, GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(check_2, GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(check_3, GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(3)
                        .addComponent(ctrlType, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE )
                        /*.addGroup(
                            createParallelGroup()

                                addComponent(pointPanel, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(polPanel, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(derivPanel, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )*/
                        .addGap(4, 4, Int.MAX_VALUE)
                )
                setVerticalGroup(
                    createSequentialGroup()
                        //.addGap(4)
                        .addGroup(
                            createParallelGroup()
                                .addComponent(check_1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGroup(
                                    createSequentialGroup()
                                        .addComponent(ctrlType, 68, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE )
                                )
                            //.addComponent(pointPanel, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )
                        //.addGap(2)
                        .addGroup(
                            createParallelGroup()
                                .addComponent(label_1,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                                .addComponent(xMin, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label_3,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                                .addComponent(xMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        )
                       // .addGap(2)
                        .addGroup(
                            createParallelGroup()
                                .addComponent(check_2, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                               // .addComponent(polPanel, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(2)
                        .addGroup(
                            createParallelGroup()
                                .addComponent(label_2, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(yMin, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label_4,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                                .addComponent(yMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(2)
                        .addGroup(
                            createParallelGroup()
                                .addComponent(check_3, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                //.addComponent(derivPanel, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )
                       // .addGap(4)



                )
            } */
        ctrlPanel.layout = GroupLayout(ctrlPanel).apply {
            setHorizontalGroup(
                createSequentialGroup()
                    .addGap(20)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(label_1,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label_2,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGap(20)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(xMin, 100,  GroupLayout.PREFERRED_SIZE,  Int.MAX_VALUE)
                            .addComponent(yMin, 100,  GroupLayout.PREFERRED_SIZE,  Int.MAX_VALUE)

                    )
                    .addGap(30, 30, Int.MAX_VALUE)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(label_3,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label_4,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGap(8)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(xMax, 100,  GroupLayout.PREFERRED_SIZE,  Int.MAX_VALUE)
                            .addComponent(yMax, 100,  GroupLayout.PREFERRED_SIZE,  Int.MAX_VALUE)
                    )
                    .addGap(40,  40, Int.MAX_VALUE)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(showPoints, GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(showPolynom, GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(showDerivative, GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGap(5)
                    .addComponent(ctrlType, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE )
                    /*.addGroup(
                        createParallelGroup()

                            addComponent(pointPanel, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(polPanel, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(derivPanel, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )*/
                    .addGap(4, 4, Int.MAX_VALUE)
            )
            setVerticalGroup(
                createParallelGroup()
                    .addGroup(
                        createSequentialGroup()
                            .addGap(20)
                            .addComponent(label_1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8)
                            .addComponent(label_2, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGroup(
                        createSequentialGroup()
                            .addGap(20)
                            .addComponent(xMin, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8)
                            .addComponent(yMin, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGroup(
                        createSequentialGroup()
                            .addGap(20)
                            .addComponent(label_3, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8)
                            .addComponent(label_4, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGroup(
                        createSequentialGroup()
                            .addGap(20)
                            .addComponent(xMax, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8)
                            .addComponent(yMax, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addGroup(
                        createSequentialGroup()
                            .addComponent(showPoints, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                            .addGap(4)
                            .addComponent(showPolynom, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                            .addGap(4)
                            .addComponent(showDerivative, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    )
                    .addGroup(
                        createSequentialGroup()
                            .addGap(4)
                            .addComponent(ctrlType, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
            )

        }
        ctrlType.layout = GroupLayout(ctrlType).apply {
            setHorizontalGroup(
                createParallelGroup()
                    .addComponent(ctrlType.POINTS, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctrlType.POLYNOM, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(ctrlType.DERIVATIVE, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            )
            setVerticalGroup(
                createSequentialGroup()
                    .addComponent(ctrlType.POINTS, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(8)
                    .addComponent(ctrlType.POLYNOM, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(8)
                    .addComponent(ctrlType.DERIVATIVE, 20, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(4)
            )
        }
        pack() // Пересчет размеров компонентов
        mainPlane.width = panel.width
        mainPlane.height = panel.height
    }
}

